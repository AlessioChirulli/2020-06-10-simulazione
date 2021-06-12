package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class Simulator {

	Map<Integer,Actor> attoriIntervistati;
	
	private int n;
	private Graph<Actor,DefaultWeightedEdge> grafo;
	private List<Actor> attoriDisponibili;
	
	private int totIntervistati;
	private int totPause;
	
	public void init(int n, Graph<Actor,DefaultWeightedEdge> grafo) {
		this.n=n;
		this.grafo=grafo;
		this.attoriDisponibili=new ArrayList<>(grafo.vertexSet());
		this.attoriIntervistati=new HashMap<>();
		totIntervistati=0;
		totPause=0;
		
	}
	
	public void run() {
		for(int i=1;i<=n;i++) {
			Random random=new Random();
			if(i==1 || !attoriIntervistati.containsKey(i-1)) {
				
				Actor a=attoriDisponibili.get(random.nextInt(attoriDisponibili.size()));
				attoriIntervistati.put(i,a);
				attoriDisponibili.remove(a);
				totIntervistati++;
				continue;
			}
			if(i>=3 && attoriIntervistati.containsKey(i-1) && attoriIntervistati.containsKey(i-2) && attoriIntervistati.get(i-1).getGender().equals(attoriIntervistati.get(i-2).getGender())) {
				if(random.nextInt(100)<90) {
					totPause++;
					continue;
				}
			}
			if(random.nextInt(100)<60) {
				Actor a=attoriDisponibili.get(random.nextInt(attoriDisponibili.size()));
				attoriIntervistati.put(i,a);
				attoriDisponibili.remove(a);
				totIntervistati++;
				continue;
			}else {
				Actor a=attoreConsigliato(i);
				if(a!=null) {
				attoriIntervistati.put(i,a);
				attoriDisponibili.remove(a);
				totIntervistati++;
				continue;
				}else {
					Actor a1=attoriDisponibili.get(random.nextInt(attoriDisponibili.size()));
					attoriIntervistati.put(i,a1);
					attoriDisponibili.remove(a1);
					totIntervistati++;
					continue;
				}
			}
			
		}
	}

	private Actor attoreConsigliato(int i) {
		int max=0;
		for(Actor a: Graphs.neighborListOf(grafo, attoriIntervistati.get(i-1))) {
			max+=grafo.getEdgeWeight(grafo.getEdge(attoriIntervistati.get(i-1), a));
		}
		for(Actor a: Graphs.neighborListOf(grafo, attoriIntervistati.get(i-1))) {
			if(max==grafo.getEdgeWeight(grafo.getEdge(attoriIntervistati.get(i-1), a))&& attoriDisponibili.contains(a))
				return a;
		}
		return null;
	}
	
	public int getTotIntervistati(){
		return this.totIntervistati;
	}
	
	public int getPause() {
		return this.totPause;
	}
	
	public String getAttoriIntervistati(){
		String s=new String();
		for(Actor a: attoriIntervistati.values()) {
			s+=a+"\n";
		}
		return s;
	}
	
}
