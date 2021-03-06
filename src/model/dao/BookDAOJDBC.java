package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert.AlertType;
import model.db.DB;
import model.entities.Book;
import model.entities.User;
import model.util.Alerts;

public class BookDAOJDBC implements DAO<Book>{

	public List<Book> findByName(String name) {

		Connection conn=null;
		PreparedStatement st = null;
		ResultSet rs = null;
		Book obj=null;
		List<Book> list = new ArrayList<Book>();
		
		try {
			if(conn==null) {
				DB DB = new DB();
				conn = DB.getConnection();
			}
			
			st = conn.prepareStatement(
				"SELECT * FROM Book WHERE Name = ?");
		
			st.setString(1, name);
			
			
			rs = st.executeQuery();
			
			while(rs.next()) {
			obj = new Book();
			
			obj.setId(rs.getInt("ID"));
		
			obj.setName(rs.getString("Name"));
			obj.setStatus(rs.getBoolean("bookStatus"));
			User user = new User();
			
			user.setID(rs.getInt("ClientID"));
		
			obj.setUser(user);
			list.add(obj);
			}
			
		}
			catch(Exception e) {
				Alerts.showAlert("Not found", "Book not found", AlertType.ERROR);
			}
		finally {
		return list;
		}
	}
	
	@Override
	public Book findByID(Integer id) {

		Connection conn=null;
		PreparedStatement st = null;
		ResultSet rs = null;
		Book obj=null;
		
		try {
			if(conn==null) {
				DB DB = new DB();
				conn = DB.getConnection();
			}
	
			st = conn.prepareStatement(
				"SELECT * FROM Book WHERE ID = ?");
		
			st.setInt(1, id);
			
			rs = st.executeQuery();
			rs.next();
			obj = new Book();
			
			obj.setId(rs.getInt("ID"));
		
			obj.setName(rs.getString("Name"));
			obj.setStatus(rs.getBoolean("bookStatus"));
			User user = new User();
			
			user.setID(rs.getInt("ClientID"));
			
			obj.setUser(user);
			
		}
			catch(Exception e) {
				Alerts.showAlert("Not found", "Data not found", AlertType.ERROR);
			}
		finally {
		return obj;
		}
	}

	@Override
	public List<Book> findAll() {
		// TODO Auto-generated method stub
		Connection conn=null;
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Book> list = new ArrayList<>();

		try {
			if(conn==null) {
				DB DB = new DB();
				conn = DB.getConnection();
			}
			st = conn.prepareStatement(
				"SELECT * FROM Book ORDER BY ID");
			rs = st.executeQuery();

			
			while (rs.next()) {
				Book obj = new Book();
				obj.setId(rs.getInt("ID"));;
				obj.setName(rs.getString("Name"));
				obj.setStatus(rs.getBoolean("bookStatus"));
				User user = new User();
				user.setID(rs.getInt("ClientID"));
				obj.setUser(user);
				
				list.add(obj);
			}
			return list;
		}
		catch (Exception e) {
			Alerts.showAlert("Not found", "Data not found", AlertType.ERROR);
			}
		
		return list;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		try {
			Connection conn;
			PreparedStatement preparedStatement;
			DB DB = new DB();
			conn = DB.getConnection();
			
			preparedStatement = conn.prepareStatement("DELETE FROM Book WHERE ID = ?"
					) ;
			
			preparedStatement.setInt(1, id);
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			Alerts.showAlert("Deleting book", "Numero de linhas afetadas: "+rowsAffected, AlertType.INFORMATION);
		
		} catch(Exception e) {
			Alerts.showAlert("Error in delete data", e.getMessage(), AlertType.ERROR);
		}
		
	}

	@Override
	public void update(Book book) {
		// TODO Auto-generated method stub
		try {
			Connection conn;
			PreparedStatement preparedStatement;
			DB DB = new DB();
			conn = DB.getConnection();
			
			if(book.getUser().getID() != 0) {
			preparedStatement = conn.prepareStatement("UPDATE Book "
					+ "SET Name = ?, "
					+ "ClientID = ?, "
					+ "bookStatus = ? "
					+ "WHERE ID = ?"
					) ;
			
			preparedStatement.setString(1, book.getName());
			preparedStatement.setInt(2, book.getUser().getID());
			preparedStatement.setBoolean(3, book.isStatus());
			preparedStatement.setInt(4, book.getId());
			
			}else {
			
				preparedStatement = conn.prepareStatement("UPDATE Book "
						+ "SET Name = ?, "
						+ "bookStatus = ? "
						+ "WHERE ID = ?"
						) ;
				
				preparedStatement.setString(1, book.getName());
				preparedStatement.setBoolean(2, book.isStatus());
				preparedStatement.setInt(3, book.getId());
					
			}
			int rowsAffected = preparedStatement.executeUpdate();
			
			//conn.close();
			//preparedStatement.close();
			
		
		} catch(Exception e) {
			System.out.println(e.getMessage());
			Alerts.showAlert("Error in UPDATE data", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public int save(Book book) {
		// TODO Auto-generated method sbookub
		
		try {
			Connection conn;
			PreparedStatement preparedStatement;
			DB DB = new DB();
			conn = DB.getConnection();
			if(book.getUser() != null) {
			preparedStatement = conn.prepareStatement("INSERT INTO Book"
					+ "(Name, clientID, bookStatus)"
					+ "VALUES"
					+ "(?,?,?)", Statement.RETURN_GENERATED_KEYS
					) ;
			
			preparedStatement.setString(1, book.getName());
			preparedStatement.setInt(2, book.getUser().getID());
			preparedStatement.setBoolean(3, book.isStatus());
			}else {
				preparedStatement = conn.prepareStatement("INSERT INTO Book"
						+ "(Name, bookStatus)"
						+ "VALUES"
						+ "(?,?)", Statement.RETURN_GENERATED_KEYS
						) ;
				
				preparedStatement.setString(1, book.getName());
				preparedStatement.setBoolean(2, book.isStatus());
					
			}
			int rowsAffected = preparedStatement.executeUpdate();
			
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
		
			//conn.close();
			//preparedStatement.close();
			
			resultSet.next();
			
		
			return resultSet.getInt(1);
		
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			Alerts.showAlert("Error in write data", e.getMessage(), AlertType.ERROR);
		}
		return -1111;
		}
		
		
	}

