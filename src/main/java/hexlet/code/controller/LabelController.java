package hexlet.code.controller;

import hexlet.code.dto.LabelDtoRequest;
import hexlet.code.model.Label;
import hexlet.code.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get a list of all labels")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", content =
            @Content(schema =
            @Schema(implementation = Label.class)))
    })
    @GetMapping("")
    public List<Label> getLabels() {
        return labelService.getLabelsList();
    }

    @Operation(summary = "Get label by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
    })
    @GetMapping(ID)
    public Label getLabel(@PathVariable Long id) {
        return labelService.getLabel(id);
    }

    @Operation(summary = "Create a new label")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Label created"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Label createLabel(@RequestBody LabelDtoRequest labelDtoRequest) {
        return labelService.createLabel(labelDtoRequest);
    }

    @Operation(summary = "Update label by ID")
    @PutMapping(ID)
    public Label updateLabel(@RequestBody LabelDtoRequest labelDtoRequest, @PathVariable Long id) {
        return labelService.updateLabel(labelDtoRequest, id);
    }

    @Operation(summary = "Delete label by ID")
    @DeleteMapping(ID)
    public void deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
    }
}
