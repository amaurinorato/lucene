package br.com.fiap.servlet;

import java.io.IOException;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.fiap.model.Search;

/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(mappedName = "java:/queue/SearchQueue")
	private Queue filaSearch;

	@Resource(mappedName = "java:/ConnectionFactory")
	private ConnectionFactory conn;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String word = "";
		try {
			System.out.println(getClass() + "Inicio........");
			Connection connection = conn.createConnection();
			try {
				Session session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				MessageProducer messageProducer = session.createProducer(filaSearch);
				ObjectMessage objMessage = session.createObjectMessage();
				Search search = new Search();
				word = request.getParameter("word");
				search.setWord(word);
				objMessage.setObject(search);
				messageProducer.send(objMessage);
				messageProducer.close();
			} finally {
				connection.close();
			}
			request.setAttribute("msg", "Pesquisa realizada com sucesso. Confira os resultados");
			request.setAttribute("word", word);
			request.getRequestDispatcher("index.jsp").forward(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
