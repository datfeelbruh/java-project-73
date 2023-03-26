package hexlet.code.controller;

import hexlet.code.dto.LabelDtoRequest;
import hexlet.code.model.Label;
import hexlet.code.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "${base-url}" + "/labels")
public class LabelController {
    public static final String LABEL_CONTROLLER_PATH = "/api/labels";
    public static final String ID = "/{id}";
    @Autowired
    private LabelService labelService;

    @GetMapping("")
    public List<Label> getLabels() {
        return labelService.getLabelsList();
    }

    @GetMapping(ID)
    public Label getLabel(@PathVariable Long id) {
        return labelService.getLabel(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Label createLabel(@RequestBody LabelDtoRequest labelDtoRequest) {
        return labelService.createLabel(labelDtoRequest);
    }

    @PutMapping(ID)
    public Label updateLabel(@RequestBody LabelDtoRequest labelDtoRequest, @PathVariable Long id) {
        return labelService.updateLabel(labelDtoRequest, id);
    }

    @DeleteMapping(ID)
    public void deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
    }
}
