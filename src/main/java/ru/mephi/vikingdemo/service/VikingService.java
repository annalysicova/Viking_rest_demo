package ru.mephi.vikingdemo.service;

import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.List;
import java.util.Optional;

@Service
public class VikingService {

    private final VikingFactory vikingFactory;
    private final VikingStorage vikingStorage;

    public VikingService(VikingFactory vikingFactory, VikingStorage vikingStorage) {
        this.vikingFactory = vikingFactory;
        this.vikingStorage = vikingStorage;
    }

    public List<Viking> findAll() {
        return vikingStorage.findAll();
    }

    public Optional<Viking> findById(int id) {
        return vikingStorage.findById(id);
    }

    public Viking create(Viking viking) {
        return vikingStorage.save(viking);
    }

    public Viking createRandomViking() {
        return create(vikingFactory.createRandomViking());
    }

    public Optional<Viking> replace(int id, Viking viking) {
        return vikingStorage.replace(id, viking);
    }

    public boolean deleteById(int id) {
        return vikingStorage.deleteById(id);
    }
}
