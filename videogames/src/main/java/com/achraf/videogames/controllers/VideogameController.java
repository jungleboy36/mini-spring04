package com.achraf.videogames.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.achraf.videogames.entities.Genre;
import com.achraf.videogames.entities.Videogame;
import com.achraf.videogames.service.VideogameService;

import jakarta.validation.Valid;
@Controller
public class VideogameController {
@Autowired
VideogameService videogameService;
@RequestMapping("/showCreate")
public String showCreate(ModelMap modelMap)
{
List<Genre> genres=videogameService.getAllGenres();
modelMap.addAttribute("mode","new");
Genre genre = new Genre();
Videogame videogame = new Videogame();
genre = genres.get(0); // prendre la première catégorie de la liste

videogame.setGenre(genre);
modelMap.addAttribute("videogame",videogame);
modelMap.addAttribute("genres",genres);
return "formVideogame";
}
@RequestMapping("/saveVideogame")
public String saveVideogame(@Valid Videogame videogame,
 BindingResult bindingResult,RedirectAttributes redirectAttributes , @RequestParam (name="size", defaultValue = "2") int size)
{
if (bindingResult.hasErrors()) return "formVideogame";

videogameService.saveVideogame(videogame);
Page<Videogame> vid = videogameService.getAllVideogamesParPage(size, size);
int totalPages = (int) vid.getTotalPages();
int lastPage = totalPages - 1;
redirectAttributes.addAttribute("page",lastPage);
return "redirect:/ListeVideogames";
}
@RequestMapping("/ListeVideogames")
public String listeVideogames(ModelMap modelMap,
@RequestParam (name="page",defaultValue = "0") int page,
@RequestParam (name="size", defaultValue = "2") int size)
{
Page<Videogame> vids = videogameService.getAllVideogamesParPage(page, size);
modelMap.addAttribute("videogames", vids);
 modelMap.addAttribute("pages", new int[vids.getTotalPages()]);
modelMap.addAttribute("currentPage", page);
return "listeVideogames";
}

@RequestMapping("/supprimerVideogame")
public String supprimerVideogame(@RequestParam("id") Long id,
ModelMap modelMap,
@RequestParam (name="page",defaultValue = "0") int page,
@RequestParam (name="size", defaultValue = "2") int size)
{
videogameService.deleteVideogameById(id);
Page<Videogame> vids = videogameService.getAllVideogamesParPage(page, size);
int totalPages = (int) vids.getTotalPages();
if (totalPages <= page) {
    page = totalPages - 1;
}
Page<Videogame> vid = videogameService.getAllVideogamesParPage(page, size);
modelMap.addAttribute("videogames", vid);
modelMap.addAttribute("pages", new int[vid.getTotalPages()]);
modelMap.addAttribute("currentPage", page);
modelMap.addAttribute("size", size);
return "listeVideogames";
}

@RequestMapping("/modifierVideogame")
public String editerVideogame(@RequestParam("id") Long id,
		 @RequestParam("page") int page,
		 ModelMap modelMap)
{
Videogame p= videogameService.getVideogame(id);
List <Genre> genres = videogameService.getAllGenres();
modelMap.addAttribute("videogame", p);
modelMap.addAttribute("mode","edit");
modelMap.addAttribute("genres",genres);
modelMap.addAttribute("page",page);
return "formVideogame";
}
@RequestMapping("/updateVideogame")
public String updateVideogame(@ModelAttribute("videogame") Videogame videogame,
@RequestParam("date") String date,ModelMap modelMap,
@RequestParam (name="page",defaultValue = "0") int page,
@RequestParam (name="size", defaultValue = "2") int size) throws ParseException 
{
	//conversion de la date 
	 SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	 Date dateCreation = dateformat.parse(String.valueOf(date));
	 videogame.setReleaseDate(dateCreation);
	 
	 videogameService.updateVideogame(videogame);
	 Page<Videogame> vids = videogameService.getAllVideogamesParPage(page,size);
	 modelMap.addAttribute("pages", new int[vids.getTotalPages()]);
	 modelMap.addAttribute("currentPage",page);
	 modelMap.addAttribute("size", size);
	 modelMap.addAttribute("videogames", vids);
	return "listeVideogames";
	}

}

