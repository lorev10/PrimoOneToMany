package it.prova.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.connection.MyConnection;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class NegozioDAO {

	public List<Negozio> list() {

		List<Negozio> result = new ArrayList<Negozio>();
		Negozio negozioTemp = null;

		try (Connection c = MyConnection.getConnection();
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery("select * from negozio a ")) {

			while (rs.next()) {
				negozioTemp = new Negozio();
				negozioTemp.setId(rs.getLong("id"));
				negozioTemp.setNome(rs.getString("nome"));
				negozioTemp.setIndirizzo(rs.getString("indirizzo"));

				result.add(negozioTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public Negozio selectById(Long idNegozioInput) {

		if (idNegozioInput == null || idNegozioInput < 1)
			return null;

		Negozio result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from negozio i where i.id=?")) {

			ps.setLong(1, idNegozioInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Negozio();
					result.setId(rs.getLong("id"));
					result.setNome(rs.getString("nome"));
					result.setIndirizzo(rs.getString("indirizzo"));
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

	public int insert(Negozio negozioInput) {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("INSERT INTO negozio (nome, indirizzo) VALUES (?, ?)")) {

			ps.setString(1, negozioInput.getNome());
			ps.setString(2, negozioInput.getIndirizzo());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public int massimochiave() {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(" select max(id) from negozio;")) {

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {

					result = rs.getInt("max(id)");
					// System.out.println(result);
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

	// TODO
	public int update(Negozio negozioInput) {

		if (negozioInput == null) {
			return 0;
		}

		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("UPDATE negozio SET nome=?, indirizzo=? where id=?;")) {

			ps.setString(1, negozioInput.getNome());
			ps.setString(2, negozioInput.getIndirizzo());
			ps.setLong(3, negozioInput.getId());
			System.out.println(negozioInput);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;

	}

	public int delete(Negozio negozioInput) {

		if (negozioInput == null) {
			return 0;
		}

		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE from negozio where id=?;")) {

			ps.setLong(1, negozioInput.getId());

			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;

	}

	// prende negozioInput e grazie al suo id va sulla tabella articoli e poi
	// ad ogni iterazione sul resultset aggiunge agli articoli di negozioInput
	public void populateArticoli(Negozio negozioInput) {
//da sistemare bisogna popolare il db
		if (negozioInput == null) {
			return;
		}

		List<Articolo> result = new ArrayList<Articolo>();

		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(" select * from articoli where negozio_id=?;")) {

			

			try (ResultSet rs = ps.executeQuery()) {

				while (rs.next()) {
					Articolo articolotemp = new Articolo();
					articolotemp.setId(rs.getLong("id"));
					articolotemp.setNome(rs.getString("nome"));
					articolotemp.setMatricola(rs.getString("matricola"));

					result.add(articolotemp);
				}

			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// implementare inoltre
	public List<Negozio> findAllByIniziali(String inizialeInput) {
		if (inizialeInput == null) {
			return null;
		}

		List<Negozio> resultarray = new ArrayList<>();
		Negozio result = null;
		String carattere = inizialeInput.charAt(0) + "%";

		try (Connection c = MyConnection.getConnection();

				PreparedStatement ps = c.prepareStatement("SELECT * from negozio where  negozio.nome LIKE ?;")) {

			ps.setString(1, carattere);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {

					result = new Negozio();
					result.setId(rs.getLong("id"));
					result.setNome(rs.getString("nome"));
					result.setIndirizzo(rs.getString("indirizzo"));
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
