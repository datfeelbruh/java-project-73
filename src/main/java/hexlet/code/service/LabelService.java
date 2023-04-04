package hexlet.code.service;

import hexlet.code.dto.LabelDtoRequest;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * A class that implements the application logic for processing requests
 * from the {@link hexlet.code.controller.LabelController}.
 * Contains a {@link LabelRepository} bean for interacting with the labels table in the database.
 *
 * @author sobadxx
 * @see hexlet.code.controller.LabelController
 */
@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    /**
     * Returns all entities of type {@link Label} from the database.
     *
     * @return all entities of type {@link Label}
     */
    public List<Label> getLabelsList() {
        return labelRepository.findAll();
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param id entity id
     * @return result of the findById method
     */
    public Label getLabel(Long id) {
        return getLabelById(id);
    }

    /**
     * Creates an entity type {@link Label} and saves it to the database.
     *
     * @param labelDtoRequest {@link LabelDtoRequest} DTO-object with the fields necessary to create a {@link Label}
     * @return created {@link Label}
     */
    public Label createLabel(LabelDtoRequest labelDtoRequest) {
        Label label = new Label();
        label.setName(labelDtoRequest.getName());
        return labelRepository.save(label);
    }

    /**
     * Updates the {@link Label} object and stores the updated {@link Label} in the database.
     *
     * @param labelDtoRequest {@link LabelDtoRequest} DTO-object with the fields necessary to update a TaskStatus
     * @param id id of the {@link Label} whose data is being updated
     * @return updated {@link Label}
     */
    public Label updateLabel(LabelDtoRequest labelDtoRequest, Long id) {
        Label labelToUpdate = getLabelById(id);

        labelToUpdate.setName(labelDtoRequest.getName());

        return labelRepository.save(labelToUpdate);
    }

    /**
     * Delete the {@link Label} object in the database.
     *
     * @param id id of the {@link Label} whose is being deleted
     */
    public void deleteLabel(Long id) {
        labelRepository.delete(getLabelById(id));
    }

    /**
     * Retrieves an entity by its id.
     *
     * @param id entity id
     * @return the entity with given id
     * @throws NoSuchElementException if Entity with {@literal id} not found
     */
    private Label getLabelById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("Label with id " + id + " not found")
                );
    }

}
