package br.com.fiap.servlet;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.fiap.dao.ResultadoDAO;
import br.com.fiap.model.Resultado;

/**
 * Servlet implementation class ResultadoServlet
 */
public class ResultadoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB
	private ResultadoDAO resultadoDAO;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResultadoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		List<Resultado> list = resultadoDAO.resutados.get(request.getParameter("word"));
		if (list == null) {
			request.setAttribute("msg", "Nenhum resultado encontrado para o termo informado ou a pesquisa ainda não terminou. Atualize a página dentro de instantes.");
		} else {
			request.setAttribute("resultados", list);
		}
		request.getRequestDispatcher("resultado.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
