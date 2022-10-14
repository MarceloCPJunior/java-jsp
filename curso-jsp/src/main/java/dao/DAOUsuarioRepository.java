package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connection.SingleConnectionBanco;
import model.ModelLogin;

public class DAOUsuarioRepository {
	private Connection connection;
	
	public DAOUsuarioRepository() {
		connection = SingleConnectionBanco.getConnection();
	}
	
	public ModelLogin gravarUseruario(ModelLogin objeto) throws SQLException {
		if(objeto.isNovo()) {
			String sql = "INSERT INTO model_login(login, senha, nome, email) VALUES (?, ?, ?, ?);";
			PreparedStatement statementinsert = connection.prepareStatement(sql);
			
			statementinsert.setString(1, objeto.getLogin());
			statementinsert.setString(2, objeto.getSenha());
			statementinsert.setString(3, objeto.getNome());
			statementinsert.setString(4, objeto.getEmail());
			
			statementinsert.execute();
			
		}else {
			String sql = "UPDATE model_login SET login=?, senha=?, nome=?, email=? WHERE login = '"+objeto.getLogin()+"';";
			PreparedStatement statementupdate = connection.prepareStatement(sql);
			
			statementupdate.setString(1, objeto.getLogin());
			statementupdate.setString(2, objeto.getSenha());
			statementupdate.setString(3, objeto.getNome());
			statementupdate.setString(4, objeto.getEmail());
			
			statementupdate.executeUpdate();
		}
		
		connection.commit();

		return this.consultaUsuario(objeto.getLogin());
		
	}
	
	public ModelLogin consultaUsuario(String login) throws SQLException {
		ModelLogin modelLogin = new ModelLogin();
		
		String sql = "SELECT * FROM model_login WHERE upper(login) = upper('"+login+"');";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		while(resultado.next()) {
			modelLogin.setId(resultado.getLong("id"));
			modelLogin.setLogin(resultado.getString("login"));
			modelLogin.setNome(resultado.getString("nome"));
			modelLogin.setEmail(resultado.getString("email"));
			modelLogin.setSenha(resultado.getString("senha"));
		}
		
		return modelLogin;
	}
	
	public boolean validaLogin(String login) throws SQLException {
		String sql = "SELECT count(1) > 0 AS existe FROM model_login WHERE upper(login) = upper('"+login+"');";
		
		PreparedStatement statement = connection.prepareStatement(sql);
		
		ResultSet resultado = statement.executeQuery();
		
		resultado.next();
		return resultado.getBoolean("existe");
	}
	
	public void deletarUser(String idUser) throws SQLException {
		String sql = "DELETE FROM model_login WHERE id = ?;";
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.executeUpdate();
		connection.commit();
	}
}
