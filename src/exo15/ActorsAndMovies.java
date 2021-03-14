package exo15;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
public class ActorsAndMovies {

	public static void main(String[] args) {
		ActorsAndMovies actorsAndMovies = new ActorsAndMovies();
        Set<Movie> movies = actorsAndMovies.readMovies();
        
        //1.Combien de films sont-ils r�f�renc�s dans ce fichier
        System.out.println("le nombre de films = " + movies.size());
        
        //2.Combien d�acteurs sont-ils r�f�renc�s dans ce fichier
        Long numberOfActors = movies.stream().flatMap(m -> m.actors().stream()).distinct().count();	
        System.out.println("\nnombre d'acteurs = " + numberOfActors );
        
        //3.Combien d�ann�es sont-elles r�f�renc�es dans ce fichier
        
        Long numberOfYears = movies.stream().map(m -> m.releaseYear()).distinct().count();
        System.out.println("\n nombre d'ann�es= " + numberOfYears );
        
        
        //4.l�ann�e de sortie du film le plus vieux; le plus r�cent
        int yearOfOldestMovie = movies.stream().mapToInt(m -> m.releaseYear()).min().orElseThrow();
        int yearOfRecentMovie = movies.stream().mapToInt(m -> m.releaseYear()).max().orElseThrow();
        		
        System.out.println("\nle film le plus vieux est sortie en " + yearOfOldestMovie
        		+ " et le plus r�cent en " + yearOfRecentMovie);
        
        //5.l'ann�e quand le plus grand nombre de films est sorti et Quel est ce nombre
        Map<Integer, Long> moviesByYears = movies.stream().collect(Collectors.groupingBy(m -> m.releaseYear(),Collectors.counting()));					
        Entry<Integer, Long> maxMoviesInAYear = moviesByYears.entrySet().stream().max(Comparator.comparing(Entry::getValue)).orElseThrow();
        System.out.println("\nl'ann�e quand le plus grand nombre de films est sorti est " + maxMoviesInAYear.getKey() 
        		+ " with " + maxMoviesInAYear.getValue() + " nombre de films r�alis�");
        
        //6.Le film avec le plus grand nombre d'acteurs et qui est ce film et ce nombre
        Movie movieWithMaxActors =movies.stream().max(Comparator.comparing(movie-> movie.actors().size())).orElseThrow();	
        System.out.println("\n Le film avec le plus grand nombre d'acteurs est" + 
        		movieWithMaxActors.title() + " avec " + movieWithMaxActors.actors().size() + " acteurs");
        		
        
        //7.L'acteur qui a jou� dans le max de films
         Map<Actor, Long> ActorsMap = movies.stream().flatMap(m -> m.actors().stream()).collect(Collectors.groupingBy(Function.identity(),Collectors.counting())); 
		   Entry<Actor, Long> ActorWithMaxMovies = ActorsMap.entrySet().stream().max(Comparator.comparing(Entry::getValue)).orElseThrow();  	
        System.out.println("\n" + ActorWithMaxMovies.getKey().firstName +" " 
         		+ ActorWithMaxMovies.getKey().lastName + " est l'acteur qui ajou� le max "
         		+ ActorWithMaxMovies.getValue() + " films");
          
        //8.L'acteur qui a jou� dans le max de films en une seule ann�e
        Collector<Movie, Integer, Entry<Actor, Long>> collector = Collectors.collectingAndThen(Collectors.flatMapping(m -> m.actors().stream(),Collectors.groupingBy(Function.identity(),Collectors.counting())),map -> map.entrySet().stream().max(Comparator.comparing(Entry::getValue)).orElseThrow());      
        Map<Integer, Entry<Actor, Long>> mapWithBigestActorPerYear =movies.stream().collect(Collectors.groupingBy(Movie::releaseYear,collector) );	         	 
        Entry <Integer, Entry<Actor, Long>> ActorWithMaxMovies2 =mapWithBigestActorPerYear.entrySet().stream().max(Comparator.comparing(e -> e.getValue().getValue())).orElseThrow();
		System.out.println("en utilisant un collector");
		System.out.println( ActorWithMaxMovies2.getValue().getKey().firstName + " "
      		+ ActorWithMaxMovies2.getValue().getKey().lastName + " c'est l'acteur qui a jou� le plus de films en ann�e"
      		+ ActorWithMaxMovies2.getValue().getValue() + " en " + ActorWithMaxMovies2.getKey());
      		
        
      
		//les deux acteurs qui ont le plus jou� ensemble
		//a
		Comparator<Actor> compareByLastName =
				  Comparator.comparing(t -> t.lastName());
		Comparator<Actor> compareByfirstName =
				  Comparator.comparing(Actor::firstName);
		  
		Comparator<Actor> comparatorOfActors =
		  compareByLastName.thenComparing(compareByfirstName);
      


}
