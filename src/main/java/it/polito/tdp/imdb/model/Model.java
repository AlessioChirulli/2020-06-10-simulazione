package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {

	private ImdbDAO dao;
	private SimpleWeightedGraph<Actor,DefaultWeightedEdge> grafo;
	private Map<Integer,Actor> idMap;
	private Simulator simulator;
	
	public Model() {
		dao = new ImdbDAO();
	}
	
	public List<String> getAllGenres(){
		 return dao.listAllGenres();
	}
	
	public void creaGrafo(String genre) {
		idMap= new HashMap<>();
		dao.listAllActors(idMap);
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		List<Actor>result=dao.getActors(idMap, genre);
		Collections.sort(result);
		Graphs.addAllVertices(grafo,result);
		
		for(Adiacenza a: dao.getAdiacenze(idMap, genre)) {
			Graphs.addEdge(this.grafo,a.getA1(), a.getA2(),a.getPeso());
		}
		
	}
	
	public int getVertexSize() {
		return this.grafo.vertexSet().size();
	}
	public int getEdgeSize() {
		return this.grafo.edgeSet().size();
	}
	public Set<Actor> getVertex(){
		return this.grafo.vertexSet();
	}
	
	public List<Actor> getAttoriSimili(Actor a){
		ConnectivityInspector<Actor,DefaultWeightedEdge> ci= new ConnectivityInspector(grafo);
		Set<Actor> result=ci.connectedSetOf(a);
		List<Actor> ordinata=new ArrayList<>();
		for(Actor attore: result) {
			ordinata.add(attore);
		}
		Collections.sort(ordinata);
		return ordinata;
		
	}
	
	public String init(int n) {
		simulator=new Simulator();
		simulator.init(n, grafo);
		simulator.run();
		return "Totale attori intervistati: "+simulator.getTotIntervistati()+"\ntotale pause: "+simulator.getPause()+"\nAttori intervistati:\n"+simulator.getAttoriIntervistati();
		
	}
}
