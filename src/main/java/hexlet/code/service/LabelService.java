package hexlet.code.service;

import hexlet.code.dto.LabelDtoRequest;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

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
        labelRepository.delete(getLabelById(id));
    }

    private Label getLabelById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("Label with id " + id + " not found")
                );
    }

}
