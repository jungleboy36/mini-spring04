package com.achraf.videogames.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.achraf.videogames.entities.Genre;
public interface GenreRepository extends JpaRepository<Genre, Long>{

}
