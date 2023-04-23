package club.p6e.coat.file.manage.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/folder")
public class FolderController {

    @GetMapping("/root")
    public void root() {

    }

    @GetMapping("/children")
    public void children() {

    }

    @PostMapping("/create/file")
    public void createFile() {

    }

    @PostMapping("/create/folder")
    public void createFolder() {

    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable String id) {

    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable String id) {

    }

}
