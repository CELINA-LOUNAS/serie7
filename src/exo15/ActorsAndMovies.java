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
        
        //1.Combien de films sont-ils référencés dans ce fichier
        System.out.println("le nombre de films = " + movies.size());
        
        //2.Combien d’acteurs sont-ils référencés dans ce fichier
        Long numberOfActors = movies.stream().flatMap(m -> m.actors().stream()).distinct().count();	
        System.out.println("\nnombre d'acteurs = " + numberOfActors );
        
        //3.Combien d’années sont-elles référencées dans ce fichier
        
        Long numberOfYears = movies.stream().map(m -> m.releaseYear()).distinct().count();
        System.out.println("\n nombre d'années= " + numberOfYears );
        
        
        //4.l’année de sortie du film le plus vieux; le plus récent
        int yearOfOldestMovie = movies.stream().mapToInt(m -> m.releaseYear()).min().orElseThrow();
        int yearOfRecentMovie = movies.stream().mapToInt(m -> m.releaseYear()).max().orElseThrow();
        		
        System.out.println("\nle film le plus vieux est sortie en " + yearOfOldestMovie
        		+ " et le plus récent en " + yearOfRecentMovie);
        
        //5.l'année quand le plus grand nombre de films est sorti et Quel est ce nombre
        Map<Integer, Long> moviesByYears = movies.stream().collect(Collectors.groupingBy(m -> m.releaseYear(),Collectors.counting()));					
        Entry<Integer, Long> maxMoviesInAYear = moviesByYears.entrySet().stream().max(Comparator.comparing(Entry::getValue)).orElseThrow();
        System.out.println("\nl'année quand le plus grand nombre de films est sorti est " + maxMoviesInAYear.getKey() 
        		+ " with " + maxMoviesInAYear.getValue() + " nombre de films réalisé");
        
        //6.Le film avec le plus grand nombre d'acteurs et qui est ce film et ce nombre
        Movie movieWithMaxActors =movies.stream().max(Comparator.comparing(movie-> movie.actors().size())).orElseThrow();	
        System.out.println("\n Le film avec le plus grand nombre d'acteurs est" + 
        		movieWithMaxActors.title() + " avec " + movieWithMaxActors.actors().size() + " acteurs");
        		
        
        //7.L'acteur qui a joué dans le max de films
         Map<Actor, Long> ActorsMap = movies.stream().flatMap(m -> m.actors().stream()).collect(Collectors.groupingBy(Function.identity(),Collectors.counting())); 
		   Entry<Actor, Long> ActorWithMaxMovies = ActorsMap.entrySet().stream().max(Comparator.comparing(Entry::getValue)).orElseThrow();  	
        System.out.println("\n" + ActorWithMaxMovies.getKey().firstName +" " 
         		+ ActorWithMaxMovies.getKey().lastName + " est l'acteur qui ajoué le max "
         		+ ActorWithMaxMovies.getValue() + " films");
          
        //8.L'acteur qui a joué dans le max de films en une seule année
        Collector<Movie, Integer, Entry<Actor, Long>> collector = Collectors.collectingAndThen(Collectors.flatMapping(m -> m.actors().stream(),Collectors.groupingBy(Function.identity(),Collectors.counting())),map -> map.entrySet().stream().max(Comparator.comparing(Entry::getValue)).orElseThrow());      
        Map<Integer, Entry<Actor, Long>> mapWithBigestActorPerYear =movies.stream().collect(Collectors.groupingBy(Movie::releaseYear,collector) );	         	 
        Entry <Integer, Entry<Actor, Long>> ActorWithMaxMovies2 =mapWithBigestActorPerYear.entrySet().stream().max(Comparator.comparing(e -> e.getValue().getValue())).orElseThrow();
		System.out.println("en utilisant un collector");
		System.out.println( ActorWithMaxMovies2.getValue().getKey().firstName + " "
      		+ ActorWithMaxMovies2.getValue().getKey().lastName + " c'est l'acteur qui a joué le plus de films en année"
      		+ ActorWithMaxMovies2.getValue().getValue() + " en " + ActorWithMaxMovies2.getKey());
      		
        
      
		//les deux acteurs qui ont le plus joué ensemble
		//a
		Comparator<Actor> compareByLastName =
				  Comparator.comparing(t -> t.lastName());
		Comparator<Actor> compareByfirstName =
				  Comparator.comparing(Actor::firstName);
		  
		Comparator<Actor> comparatorOfActors =
		  compareByLastName.thenComparing(compareByfirstName);
      


}
