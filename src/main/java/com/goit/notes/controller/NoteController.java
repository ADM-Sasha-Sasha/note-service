package com.goit.notes.controller;

import com.goit.notes.entity.Note;

import com.goit.notes.entity.User;
import com.goit.notes.service.NoteService;
import com.goit.notes.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("/note")
@Slf4j
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;
    private User user;

    @GetMapping("/listNotes")
    public ModelAndView listAllNotes(Model model, ModelAndView modelAndView) {

        Optional<User> authorizedUser = userService.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
        authorizedUser.ifPresent(entity -> user = entity);

        model.addAttribute("title", "List Notes");
        model.addAttribute("message", "Hello " + user.getUserName().toUpperCase() + " this is your list notes");

        modelAndView.addObject("notes", user.getNotes());
        modelAndView.setViewName("listNotes");
        return modelAndView;
    }

    @GetMapping("/createNote")
    public String create(Model model) {
        model.addAttribute("title", "Create Note");
        model.addAttribute("message", "Add new note");

        return "createNote";
    }

    @PostMapping("/create")
    public String createNote(@Valid Note note) {
        noteService.save(note);
        return "redirect:/notes";
    }

    @GetMapping("/edit")
    public String edit(UUID id, Model model) {
        Note note = noteService.getById(id);
        model.addAttribute("message", "Edit note");
        model.addAttribute("id", note.getId());
        model.addAttribute("name", note.getName());
        model.addAttribute("description", note.getDescription());
        model.addAttribute("access", note.getAccess());
        return "edit";
    }

    @PostMapping("/edit")
    public String editNote(@Valid Note note) {
        //todo
        return "redirect:/notes";
    }

    @GetMapping("/share/{id}")
    public String shareNote(@PathVariable UUID id, Note note) {
        //todo
        return "share";
    }

    @ModelAttribute("note")
    public Note defaultNote() {
        return new Note();
    }

}

// добавить метод удаления по ид