package it.prova.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.connection.MyConnection;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class ArticoloDAO {

	public List<Articolo> list() {

		List<Articolo> result = new ArrayList<Articolo>();

		try (Connection c = MyConnection.getConnection();
				Statement s = c.createStatement();
				//STRATEGIA EAGER FETCHING
				ResultSet rs = s
						.executeQuery("select * from articolo a inner join negozio n on n.id=a.negozio_id")) {

			while (rs.next()) {
				Articolo articoloTemp = new Articolo();
				articoloTemp.setNome(rs.getString("NOME"));
				articoloTemp.setMatricola(rs.getString("matricola"));
				articoloTemp.setId(rs.getLong("a.id"));

				Negozio negozioTemp = new Negozio();
				negozioTemp.setId(rs.getLong("n.id"));
				negozioTemp.setNome(rs.getString("nome"));
				negozioTemp.setIndirizzo(rs.getString("indirizzo"));

				articoloTemp.setNegozio(negozioTemp);
				result.add(articoloTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			//rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public Articolo selectById(Long idArticoloInput) {

		if (idArticoloInput == null || idArticoloInput < 1)
			return null;

		Articolo result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from articolo a where a.id=?")) {

			ps.setLong(1, idArticoloInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Articolo();
					result.setNome(rs.getString("nome"));
					result.setMatricola(rs.getString("matricola"));
					result.setId(rs.getLong("id"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			//rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	public int insert(Articolo articoloInput) {

		if (articoloInput.getNegozio() == null || articoloInput.getNegozio().getId() < 1)
			return -1;

		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c
						.prepareStatement("INSERT INTO articolo (nome, matricola,negozio_id) VALUES (?, ?, ?)")) {
			//System.out.println( articoloInput.getNegozio().getId());
			ps.setString(1, articoloInput.getNome());
			ps.setString(2, articoloInput.getMatricola());
			ps.setLong(3, articoloInput.getNegozio().getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			//rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	// TODO

	public Articolo selectByIdWithJoin(Long idInput) {

		if (idInput == null | idInput < 1)
			return null;

		Articolo result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from articolo a inner join negozio n on ?=a.negozio_id;")) {

			ps.setLong(1,idInput );
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Articolo();
					result.setId(rs.getLong("id"));
					
					result.setMatricola(rs.getString("matricola"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public int update(Articolo articoloInput) {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("INSERT INTO articolo (nome, matricola) VALUES (?, ?)")) {

			ps.setString(1, articoloInput.getNome());
			ps.setString(2, articoloInput.getMatricola());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public int delete(Articolo articoloInput) {
		if (articoloInput == null) {
			return 0;
		}
System.out.println(articoloInput.getId());
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE from articolo where id=?;")) {

			ps.setLong(1, articoloInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	
	public int massimochiave(int chiave) {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(" select max(id) from negozio WHERE 	?= id ")) {
			ps.setLong(1, chiave);
			
			
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					
					result=rs.getInt("max(id)");
					System.out.println(result);
				} else {
					result = 0;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
	
	// implementare inoltre
	public List<Articolo> findAllByNegozio(Negozio negozioInput) {
		if (negozioInput == null) {
			return null;
		}
	
		List<Articolo> resultarray = new ArrayList<>();
		Articolo result = null;
		
		
		try (Connection c = MyConnection.getConnection();

				PreparedStatement ps = c.prepareStatement("select articolo.* from articolo,negozio WHERE negozio_id = ?;")) {
			ps.setLong(1, negozioInput.getId());
		//ps.setLong(1, 5);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {

					result = new Articolo();
					result.setId(rs.getLong("id"));
					
					//System.out.println(rs.getLong("matricola"));
					
					result.setNegozio(negozioInput);
					resultarray.add(result);
					

				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return resultarray;

	}

	public List<Articolo> findAllByMatricola(String matricolaInput) {
		
		if (matricolaInput == null)
			return null;

		Articolo result = null;
		List<Articolo> resultarray = new ArrayList<>();
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select articolo.* from articolo where articolo.matricola=?;")) {

			ps.setString(1, matricolaInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Articolo();
					result.setId(rs.getLong("id"));
					result.setNome(rs.getString("nome"));
					result.setMatricola(matricolaInput);
					resultarray.add(result);
				} else {
					resultarray = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return resultarray;
		
		
	}

	public List<Articolo> findAllByIndirizzoNegozio(String indirizzoNegozioInput) {
		if (indirizzoNegozioInput == null) {
			return null;
		}
	
		List<Articolo> resultarray = new ArrayList<>();
		Articolo result = null;
		
		
		try (Connection c = MyConnection.getConnection();

				PreparedStatement ps = c.prepareStatement("select articolo.* from negozio INNER JOIN articolo ON negozio.id = articolo.negozio_id  where negozio.indirizzo=? ;")) {
			ps.setString(1, indirizzoNegozioInput);
		//ps.setLong(1, 5);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {

					result = new Articolo();
					result.setId(rs.getLong("id"));
					
					//System.out.println(rs.getLong("matricola"));
					result.setNome(rs.getString("nome"));
					result.setMatricola(rs.getString("matricola"));
					resultarray.add(result);
					

				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return resultarray;

	}

}
