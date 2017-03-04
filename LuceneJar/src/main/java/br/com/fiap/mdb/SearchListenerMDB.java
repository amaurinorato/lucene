package br.com.fiap.mdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import br.com.fiap.dao.ResultadoDAO;
import br.com.fiap.model.Resultado;
import br.com.fiap.model.Search;

/**
 * Message-Driven Bean implementation class for: SearchListenerMDB
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue"), 
				@ActivationConfigProperty(
						propertyName = "destination", propertyValue = "queue/SearchQueue")
		})
public class SearchListenerMDB implements MessageListener {

	@EJB
	private ResultadoDAO resultadoDAO;
	
	static {
		try {  
			Path path = Paths.get("C:/lucene/indexes");
			Directory directory = FSDirectory.open(path);
			IndexWriterConfig config = new IndexWriterConfig(new SimpleAnalyzer());        
			IndexWriter indexWriter = new IndexWriter(directory, config);
			indexWriter.deleteAll();
			File f = new File("C:/lucene/sample"); // current directory     
			for (File file : f.listFiles()) {
				Document doc = new Document();
				doc.add(new TextField("path", file.getAbsolutePath(), Store.YES));
				doc.add(new TextField("filename", file.getName(), Store.YES));
				FileInputStream is = new FileInputStream(file);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuffer stringBuffer = new StringBuffer();
				String line = null;
				while((line = reader.readLine())!=null){
					stringBuffer.append(line).append("\n");
				}
				reader.close();
				doc.add(new TextField("contents", stringBuffer.toString(), Store.YES));
				indexWriter.addDocument(doc);           
			}               
			indexWriter.close();           
			directory.close();
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}

	/**
	 * Default constructor. 
	 */
	public SearchListenerMDB() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see MessageListener#onMessage(Message)
	 */
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				System.out.println("Queue: TextMessage recebida em " + new Date());
				TextMessage msg = (TextMessage) message;
				System.out.println("Message is : " + msg.getText());
			} else if (message instanceof ObjectMessage) {
				ObjectMessage msg = (ObjectMessage) message;
				Search messageObject = (Search) msg.getObject();
				String word = messageObject.getWord();
				resultadoDAO.resutados.remove(word);
				search(word);
			} else {
				System.out.println("Nenhuma mensagem v�lida!");
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void search(String text) {   
		//Apache Lucene searching text inside .txt files
		try {   
			String folder = "C://lucene/indexes";
			Path path = Paths.get(folder);
			Directory directory = FSDirectory.open(path);       
			IndexReader indexReader =  DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			QueryParser queryParser = new QueryParser("contents",  new StandardAnalyzer());  
			Query query = queryParser.parse(text);
			TopDocs topDocs = indexSearcher.search(query,10);

			List<Resultado> resultados = new ArrayList<Resultado>();;
			
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				Document document = indexSearcher.doc(scoreDoc.doc);
				Resultado r = new Resultado();
				r.setNomeArquivo(document.get("filename"));
				r.setLink(document.get("path"));
				resultados.add(r);
			}
			
			if (!resultados.isEmpty()) {
				this.resultadoDAO.resutados.put(text, resultados);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}               
	}
}
