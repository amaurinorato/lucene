package br.com.fiap.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
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
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		try {
			System.out.println(getClass() + "Inicio........");
			Connection connection = conn.createConnection();
			try {
				Session session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				MessageProducer messageProducer = session.createProducer(filaSearch);
				//1. Enviando objeto TextMessage
				TextMessage message = session.createTextMessage();
				message.setText("Exemplo EJB3 MDB Queue!!!");
				messageProducer.send(message);
				out.println("1. Enviando mensagem tipo TextMessage");
				//2. Enviando objeto ObjectMessage
				ObjectMessage objMessage = session.createObjectMessage();
				Search search = new Search();
				search.setWord("java");
				objMessage.setObject(search);
				messageProducer.send(objMessage);
				messageProducer.close();
			} finally {
				connection.close();
			}
			System.out.println(getClass() + "Fim........");
			out.print("<H1>Objeto enviado com sucesso! JMS 1.0</H1>");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
