package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Cliente;
import persistence.GenericDao;
import persistence.ClienteDao;

@WebServlet("/cliente")
public class ClienteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ClienteServlet() {
        super();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
    	RequestDispatcher rd = request.getRequestDispatcher("cliente.jsp");
		rd.forward(request, response);
    }
    
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//entrada
		String cmd = request.getParameter("botao");
		String cpf = request.getParameter("cpf");
		String nome = request.getParameter("nome");
		String email =  request.getParameter("email");
		String limite_de_credito =  request.getParameter("limite_de_credito");
		String dt_nascimento = request.getParameter("dt_nascimento");


		
		//saida
		String saida="";
		String erro="";
		Cliente ct = new Cliente();
		List<Cliente> clientes = new ArrayList<>();
		
		if(!cmd.contains("Listar")) {
			ct.setCpf(cpf);
		}
		if(cmd.contains("Cadastrar") || cmd.contains("Alterar")){
			ct.setNome(nome);
			ct.setEmail(email);
			ct.setLimite_de_credito(limite_de_credito);
			ct.setDt_nascimento(dt_nascimento);
		}
		try {
			if (cmd.contains("Cadastrar")) {
				saida = cadastrarCliente(ct);
				ct = null;
			}
			if (cmd.contains("Alterar")) {
				saida = alterarCliente(ct);
				ct = null;
			}
			if (cmd.contains("Excluir")) {
				saida = excluirCliente(ct);
				ct = null;
			}
			if (cmd.contains("Buscar")) {
				ct = buscarCliente(ct);
			}
			if (cmd.contains("Listar")) {
			    clientes = listarCliente();
			}
		} catch(SQLException | ClassNotFoundException e) {
			erro = e.getMessage();
		} finally {
			request.setAttribute("saida", saida);
			request.setAttribute("erro", erro);
			request.setAttribute("cliente", ct);
			request.setAttribute("clientes", clientes);
			
			RequestDispatcher rd = request.getRequestDispatcher("cliente.jsp");
			rd.forward(request, response);
		}
	}

	private String cadastrarCliente(Cliente ct)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
	    ClienteDao pDao = new ClienteDao (gDao);
		String saida = pDao.iudCliente("I", ct);
		return saida;
		
	}

	private String alterarCliente(Cliente ct)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao pDao = new ClienteDao (gDao);
		String saida = pDao.iudCliente("U", ct);
		return saida;
		
	}

	private String excluirCliente(Cliente ct)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao pDao = new ClienteDao (gDao);
		String saida = pDao.iudCliente("D", ct);
		return saida;
		
	}

	private Cliente buscarCliente (Cliente ct)throws SQLException, ClassNotFoundException {
		GenericDao gDao = new GenericDao();
		ClienteDao pDao = new ClienteDao (gDao);
		ct = pDao.consultar(ct);
		return ct;
	
	}

	private List<Cliente> listarCliente()throws SQLException, ClassNotFoundException {
		
		GenericDao gDao = new GenericDao();
		ClienteDao pDao = new ClienteDao (gDao);
		List<Cliente> clientes = pDao.listar();
		
	 return clientes;
	}

}