package persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.List;
import model.Cliente;


public class ClienteDao implements ICrud<Cliente>, IClienteDao {
	
private GenericDao gDao;
	

	public ClienteDao(GenericDao gDao) {
		this.gDao = gDao;
	}
	
	

	@Override
	public void inserir(Cliente ct) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "INSERT INTO cliente VALUES (?,?,?,?,?)";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, ct.getCpf());
		ps.setString(2, ct.getNome());
		ps.setString(3, ct.getEmail());
		ps.setString(4, ct.getLimite_de_credito());
		ps.setString(5, ct.getDt_nascimento()); 
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void atualizar(Cliente ct) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "UPDATE cliente SET nome = ?, email = ?, limite_de_credito = ?, dt_nascimento = ? WHERE cpf = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, ct.getNome());
		ps.setString(2, ct.getEmail());
		ps.setString(3, ct.getLimite_de_credito());
		ps.setString(4, ct.getDt_nascimento()); 
		ps.setString(5, ct.getCpf());
		ps.execute();
		ps.close();
		c.close();
	}

	@Override
	public void excluir(Cliente ct) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "DELETE cliente WHERE cpf = ?";
		PreparedStatement ps = c.prepareStatement(sql);
		ps.setString(1, ct.getCpf());
		ps.execute();
		ps.close();
		c.close();
		
	}

	@Override
	public Cliente consultar(Cliente ct) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "SELECT cpf, nome, email, limite_de_credito, dt_nascimento FROM cliente WHERE cpf = ?";
		PreparedStatement ps = c.prepareStatement(sql);
	    ps.setString(1, ct.getCpf());
	    ResultSet rs = ps.executeQuery();
	     if (rs.next()) {
	    	 ct.setCpf(rs.getString("cpf"));;
	    	 ct.setNome(rs.getString("nome"));
	    	 ct.setEmail(rs.getString("email"));
	    	 ct.setLimite_de_credito(rs.getString("limite_de_credito"));
	    	 ct.setDt_nascimento(rs.getString("dt_nascimento"));
	     }
	        rs.close();
			ps.close();
			c.close();
		return ct;
	}

	@Override
	public List<Cliente> listar() throws SQLException, ClassNotFoundException {
		
		List<Cliente> clientes = new ArrayList<>();	
		Connection c = gDao.getConnection();
		String sql = "SELECT cpf, nome, email, limite_de_credito, dt_nascimento FROM cliente";
		PreparedStatement ps = c.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		 while (rs.next()) {
			 
			 Cliente ct = new Cliente();
			 ct.setCpf(rs.getString("cpf"));;
	    	 ct.setNome(rs.getString("nome"));
	    	 ct.setEmail(rs.getString("email"));
	    	 ct.setLimite_de_credito(rs.getString("limite_de_credito"));
	    	 ct.setDt_nascimento(rs.getString("dt_nascimento"));
			 clientes.add(ct);
		 }
		 rs.close();
		 ps.close();
		 c.close();
		return clientes;
	}



	@Override
	public String iudCliente(String op, Cliente ct) throws SQLException, ClassNotFoundException {
		Connection c = gDao.getConnection();
		String sql = "CALL GerenciarCliente (?,?,?,?,?,?,?)";
		CallableStatement cs = c.prepareCall(sql);
		cs.setString(1, op);
		cs.setString(2, ct.getCpf());
		cs.setString(3, ct.getNome());
		cs.setString(4, ct.getEmail());	
		cs.setString(5, ct.getLimite_de_credito()); 
		cs.setString(6, ct.getDt_nascimento()); 
		cs.registerOutParameter(7, Types.VARCHAR);
		cs.execute();
		String saida = cs.getString(7);
		cs.close();
		c.close();
		return saida;
	}

}