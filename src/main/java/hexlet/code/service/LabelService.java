package hexlet.code.service;

import hexlet.code.dto.LabelDtoRequest;
import hexlet.code.exception.CustomConstraintException;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private TaskRepository taskRepository;

    public List<Label> getLabelsList() {
        return labelRepository.findAll();
    }

    public Label getLabel(Long id) {
        return getLabelById(id);
    }

    public Label createLabel(LabelDtoRequest labelDtoRequest) {
        Label label = new Label();
        label.setName(labelDtoRequest.getName());
        return labelRepository.save(label);
    }

    public Label updateLabel(LabelDtoRequest labelDtoRequest, Long id) {
        Label labelToUpdate = getLabelById(id);

        labelToUpdate.setName(labelDtoRequest.getName());

        return labelRepository.save(labelToUpdate);
    }

    public void deleteLabel(Long id) {
        Label label = getLabelById(id);

        if (checkLabelAndTaskAssociations(label.getId())) {
            throw new CustomConstraintException("Unable to delete the label status associated with the any task");
        }

        labelRepository.delete(getLabelById(id));
    }

    private Label getLabelById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Label with id " + id + " not found")
                );
    }

    private boolean checkLabelAndTaskAssociations(Long labelId) {
        long countLabelSameId = taskRepository.findAll()
                .stream()
                .map(Task::getTaskStatus)
                .filter(e -> e.getId() == labelId)
                .count();


        return countLabelSameId != 0;
    }
}
