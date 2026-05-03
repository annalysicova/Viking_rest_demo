package ru.mephi.vikingdemo.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.EquipmentItemEntity;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.VikingEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class VikingStorage {

    private final VikingRepository vikingRepository;
    private final EquipmentItemRepository equipmentItemRepository;
    private final VikingMapper vikingMapper;

    public VikingStorage(
            VikingRepository vikingRepository,
            EquipmentItemRepository equipmentItemRepository,
            VikingMapper vikingMapper
    ) {
        this.vikingRepository = vikingRepository;
        this.equipmentItemRepository = equipmentItemRepository;
        this.vikingMapper = vikingMapper;
    }

    @Transactional
    public Viking save(Viking viking) {
        Integer vikingId = vikingRepository.save(vikingMapper.toVikingEntity(viking));
        saveEquipment(vikingId, viking.equipment());
        return findById(vikingId).orElseThrow();
    }

    public List<Viking> findAll() {
        List<VikingEntity> vikingEntities = vikingRepository.findAll();
        List<EquipmentItemEntity> equipmentEntities = equipmentItemRepository.findAll();

        Map<Integer, List<EquipmentItemEntity>> equipmentByVikingId = equipmentEntities.stream()
                .collect(Collectors.groupingBy(EquipmentItemEntity::vikingId));

        return vikingEntities.stream()
                .map(vikingEntity -> vikingMapper.toViking(
                        vikingEntity,
                        equipmentByVikingId.getOrDefault(vikingEntity.id(), List.of())
                ))
                .toList();
    }

    public Optional<Viking> findById(int id) {
        return vikingRepository.findById(id)
                .map(entity -> vikingMapper.toViking(entity, equipmentItemRepository.findByVikingId(id)));
    }

    @Transactional
    public Optional<Viking> replace(int id, Viking viking) {
        if (vikingRepository.findById(id).isEmpty()) {
            return Optional.empty();
        }

        vikingRepository.update(vikingMapper.toVikingEntity(id, viking));
        equipmentItemRepository.deleteByVikingId(id);
        saveEquipment(id, viking.equipment());

        return findById(id);
    }

    @Transactional
    public boolean deleteById(int id) {
        if (vikingRepository.findById(id).isEmpty()) {
            return false;
        }
        vikingRepository.deleteById(id);
        return true;
    }

    private void saveEquipment(Integer vikingId, List<EquipmentItem> equipment) {
        if (equipment == null) {
            return;
        }
        for (EquipmentItem item : equipment) {
            equipmentItemRepository.save(vikingMapper.toEquipmentItemEntity(vikingId, item));
        }
    }
}
