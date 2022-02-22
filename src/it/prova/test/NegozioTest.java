package it.prova.test;

import java.util.ArrayList;
import java.util.List;

import it.prova.dao.ArticoloDAO;
import it.prova.dao.NegozioDAO;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class NegozioTest {

	public static void main(String[] args) {
		NegozioDAO negozioDAOInstance = new NegozioDAO();
		ArticoloDAO articoloDAOInstance = new ArticoloDAO();

		// ora con i dao posso fare tutte le invocazioni che mi servono
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testInserimentoNegozio(negozioDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		testFindByIdNegozio(negozioDAOInstance);

		testInsertArticolo(negozioDAOInstance, articoloDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testFindByIdArticolo(articoloDAOInstance);
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		// ESERCIZIO: COMPLETARE DAO E TEST RELATIVI

		// ESERCIZIO SUCCESSIVO
		/*
		 * se io voglio caricare un negozio e contestualmente anche i suoi articoli
		 * dovrò sfruttare il populateArticoli presente dentro negoziodao. Per esempio
		 * Negozio negozioCaricatoDalDb = negozioDAOInstance.selectById...
		 * 
		 * negozioDAOInstance.populateArticoli(negozioCaricatoDalDb);
		 * 
		 * e da qui in poi il negozioCaricatoDalDb.getArticoli() non deve essere più a
		 * size=0 (se ha articoli ovviamente) LAZY FETCHING (poi ve lo spiego)
		 */

		testAggiornamentoNegozio(negozioDAOInstance);

		System.out.println("ricerchiamo per iniziale");
		testRicercaPerIniziale(negozioDAOInstance);
		 testInsertArticolo(negozioDAOInstance, articoloDAOInstance);
		//testFindByIdArticolo(articoloDAOInstance);
		 testEliminaNegozio(negozioDAOInstance);
		// testEliminaArticolo(negozioDAOInstance, articoloDAOInstance);
		//testFindByIdArticolo(negozioDAOInstance, articoloDAOInstance);
		//testFindArticoloByNegozio(negozioDAOInstance, articoloDAOInstance);
		testFindArticoloByIndirizzo(negozioDAOInstance, articoloDAOInstance);
	}

	private static void testInserimentoNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testInserimentoNegozio inizio.............");
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("Negozio1", "via dei mille 14"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");

		System.out.println(".......testInserimentoNegozio fine: PASSED.............");
	}

	private static void testAggiornamentoNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testaggiornamento inizio.............");
		Negozio negoziodaaggiornare = new Negozio("Nuovo", "via dei duemila 14");
		
	//	negoziodaaggiornare.setId((long) 1);
		
		negozioDAOInstance.insert(negoziodaaggiornare);
		negoziodaaggiornare.setId((long)negozioDAOInstance.massimochiave());
		int risultato = negozioDAOInstance.update(negoziodaaggiornare);
		if (risultato < 1)
			throw new RuntimeException("Test aggiornamento : FAILED");

		System.out.println(".......testInserimentoNegozio fine: PASSED.............");
	}

	private static void testRicercaPerIniziale(NegozioDAO negozioDAOInstance) {

		System.out.println(".......testRicercaNegozio inizio.............");

		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("Conad", "via dei mille 14"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");

		int quantiNegoziInseritidue = negozioDAOInstance.insert(new Negozio("Coop", "via dei mille 14"));
		if (quantiNegoziInseritidue < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");

		List<Negozio> resultarray = negozioDAOInstance.findAllByIniziali("coop");

		if (resultarray == null) {
			throw new RuntimeException("testFindByIdNegozio : FAILED, la ricerca non ha prodotto risultati");
		}

		if (2 < resultarray.size()) {

			System.out.println(".......testRicercaNegozio fine: PASSED.............");
		} else {
			System.out.println("..........erroreTestricerca");
		}
	}

	private static void testFindByIdNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testFindByIdNegozio inizio.............");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testFindByIdNegozio : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		Negozio negozioCheRicercoColDAO = negozioDAOInstance.selectById(primoNegozioDellaLista.getId());
		if (negozioCheRicercoColDAO == null
				|| !negozioCheRicercoColDAO.getNome().equals(primoNegozioDellaLista.getNome()))
			throw new RuntimeException("testFindByIdNegozio : FAILED, i nomi non corrispondono");

		System.out.println(".......testFindByIdNegozio fine: PASSED.............");
	}

	public static void testFindArticoloByNegozio(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testtrovaarticoloByNegozio inizio.............");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testFindByIdNegozio : FAILED, non ci sono negozi sul DB");

		List<Articolo> articolipresenti = articoloDAOInstance.list();
		if (articolipresenti.size() < 1)
			throw new RuntimeException("testFindByIdArticoli : FAILED, non ci sono articoli sul DB");

		Negozio ultimoNegozioDellaLista = new Negozio();
		ultimoNegozioDellaLista.setIndirizzo("via natale");
		ultimoNegozioDellaLista.setNome("test");
		int quantiNegoziInseriti = negozioDAOInstance.insert(ultimoNegozioDellaLista);
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");
		ultimoNegozioDellaLista.setId((long) negozioDAOInstance.massimochiave());

		Articolo daricercareconilnegozio = new Articolo("articolosdd", "dzcx", ultimoNegozioDellaLista);
		int quantiArticoliInseriti = articoloDAOInstance.insert(daricercareconilnegozio);
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("testInsertArticolo : FAILED");

		List<Articolo> articolipresentilista = articoloDAOInstance.findAllByNegozio(ultimoNegozioDellaLista);
		if (articolipresentilista == null)
			throw new RuntimeException("testFindByIdNegozio : FAILED, Errore nella ricerca");

		if (articolipresentilista.size() < 1)
			throw new RuntimeException("testFindByIdNegozio : FAILED, Errore nella ricerca");

		System.out.println(".....................ricercaconilnegozioriuscita...................");

	}

	private static void testFindByIdArticolo(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testFindByIdArticolo inizio.............");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testFindByIdNegozio : FAILED, non ci sono negozi sul DB");

		List<Articolo> articolipresenti = articoloDAOInstance.list();
		if (articolipresenti.size() < 1)
			throw new RuntimeException("testFindByIdArticoli : FAILED, non ci sono articoli sul DB");

		Negozio ultimoNegozioDellaLista = new Negozio();
		ultimoNegozioDellaLista.setIndirizzo("via natale");
		ultimoNegozioDellaLista.setNome("test");
		int quantiNegoziInseriti = negozioDAOInstance.insert(ultimoNegozioDellaLista);
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");
		ultimoNegozioDellaLista.setId((long) negozioDAOInstance.massimochiave());

		int quantiArticoliInseriti = articoloDAOInstance
				.insert(new Articolo("articolo1", "matricola1", ultimoNegozioDellaLista));
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("testInsertArticolo : FAILED");

		Articolo ricercato = articoloDAOInstance.selectByIdWithJoin(ultimoNegozioDellaLista.getId());
		if (ricercato == null)
			throw new RuntimeException("testFindByIdNegozio : FAILED, i nomi non corrispondono");
		System.out.println(".......findbyarticolo fine: PASSED.............");
		// System.out.println(ultimoNegozioDellaLista.getMatricola());
		// List<Articolo> articolipresentilista =
		// articoloDAOInstance.findAllByNegozio(ultimoNegozioDellaLista);
		// if (articolipresentilista == null)
		// throw new RuntimeException("testFindByIdNegozio : FAILED, i nomi non
		// corrispondono");
		// System.out.println(articolipresentilista.size());
		// if (articolipresentilista.size() < 1)
		// throw new RuntimeException("testFindByIdNegozio : FAILED, i nomi non
		// corrispondono");
		// System.out.println(".......findbyarticolo fine: PASSED.............");
	}

	private static void testInsertArticolo(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testInsertArticolo inizio.............");

		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testInsertArticolo : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		int quantiArticoliInseriti = articoloDAOInstance
				.insert(new Articolo("articolo1", "matricola1", primoNegozioDellaLista));
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("testInsertArticolo : FAILED");

		System.out.println(".......testInsertArticolo fine: PASSED.............");
	}

	private static void testEliminaArticolo(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testaggiornamento cancellazione negozio.............");
		int inizionumeroarticoli = articoloDAOInstance.list().size();
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testInsertArticolo : FAILED, non ci sono negozi sul DB");

		Negozio negoziodacancellare = new Negozio("Nuovo", "via dei duemila 14");

		negoziodacancellare.setId((long) negozioDAOInstance.massimochiave());
		Articolo articolodacancellare = new Articolo("pallone", "matricola", negoziodacancellare);
		articolodacancellare.setId((long) 1L);
		int risultato = articoloDAOInstance.delete(articolodacancellare);
		if (risultato < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");
		int finenumeroarticoli = articoloDAOInstance.list().size();
		if (finenumeroarticoli >= inizionumeroarticoli) {
			throw new RuntimeException("testInserimentoNegozio : FAILED");
		}

		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");
		System.out.println(".......testInserimentoNegozio fine: PASSED.............");

	}

	private static void testFindByIdArticolo(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testFindByIdArticolo inizio.............");
		List<Articolo> elencoArticoliPresenti = articoloDAOInstance.list();
		if (elencoArticoliPresenti.size() < 1)
			throw new RuntimeException("testFindByIdArticolo : FAILED, non ci sono articoli sul DB");

		Articolo primoArticoloDellaLista = elencoArticoliPresenti.get(0);

		Articolo articoloCheRicercoColDAO = articoloDAOInstance.selectById(primoArticoloDellaLista.getId());
		if (articoloCheRicercoColDAO == null
				|| !articoloCheRicercoColDAO.getNome().equals(primoArticoloDellaLista.getNome()))
			throw new RuntimeException("testFindByIdArticolo : FAILED, i nomi non corrispondono");

		System.out.println(".......testFindByIdArticolo fine: PASSED.............");
	}

	// il metodo negozio crea ed elimina un negozio con la funzione chiave max
	private static void testEliminaNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testaggiornamento cancellazione negozio.............");

		int quantiNegoziDaCancellare = negozioDAOInstance.insert(new Negozio("Negozio1", "via dei mille 14"));
		if (quantiNegoziDaCancellare < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");

		Negozio negoziodacancellare = new Negozio("Nuovo", "via dei duemila 14");

		negoziodacancellare.setId((long) negozioDAOInstance.massimochiave());
		int risultato = negozioDAOInstance.delete(negoziodacancellare);
		if (risultato < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");

		System.out.println(".......testInserimentoNegozio fine: PASSED.............");
	}

	public static void testFindArticoloByIndirizzo(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testtrovaarticoloByIndirizzo inizio.............");

		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testFindByIdNegozio : FAILED, non ci sono negozi sul DB");

		Negozio ultimoNegozioDellaLista = new Negozio();
		ultimoNegozioDellaLista.setIndirizzo("via indirizzo");
		ultimoNegozioDellaLista.setNome("test");

		int quantiNegoziInseriti = negozioDAOInstance.insert(ultimoNegozioDellaLista);

		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");

		ultimoNegozioDellaLista.setId((long) negozioDAOInstance.massimochiave());

		Articolo daricercareconilnegozio = new Articolo("articolosdd", "dzcx", ultimoNegozioDellaLista);
		
		int quantiArticoliInseriti = articoloDAOInstance.insert(daricercareconilnegozio);
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("testInsertArticolo : FAILED");

		List<Articolo> articolipresentilista = articoloDAOInstance.findAllByIndirizzoNegozio(ultimoNegozioDellaLista.getIndirizzo());
		if (articolipresentilista == null)
			throw new RuntimeException("testFindByIdNegozio : FAILED, Errore nella ricerca");

		if (articolipresentilista.size() < 1)
			throw new RuntimeException("testFindByIdNegozio : FAILED, Errore nella ricerca");
		//System.out.println(articolipresentilista);
		System.out.println(".....................ricercaconilnegozioriuscita...................");

	}

}
