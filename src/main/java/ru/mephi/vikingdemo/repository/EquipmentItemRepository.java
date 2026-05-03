package ru.mephi.vikingdemo.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.mephi.vikingdemo.model.EquipmentItemEntity;

import java.util.List;

@Repository
public class EquipmentItemRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<EquipmentItemEntity> equipmentRowMapper = (rs, rowNum) -> new EquipmentItemEntity(
            rs.getInt("id"),
            rs.getInt("viking_id"),
            rs.getString("name"),
            rs.getString("quality")
    );

    public EquipmentItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<EquipmentItemEntity> findByVikingId(int vikingId) {
        String sql = """
                select id, viking_id, name, quality
                from equipment_items
                where viking_id = ?
                order by id
                """;
        return jdbcTemplate.query(sql, equipmentRowMapper, vikingId);
    }

    public List<EquipmentItemEntity> findAll() {
        String sql = """
                select id, viking_id, name, quality
                from equipment_items
                order by viking_id, id
                """;
        return jdbcTemplate.query(sql, equipmentRowMapper);
    }

    public void save(EquipmentItemEntity item) {
        String sql = """
                insert into equipment_items(viking_id, name, quality)
                values (?, ?, ?)
                """;
        jdbcTemplate.update(sql, item.vikingId(), item.name(), item.quality());
    }

    public int deleteByVikingId(int vikingId) {
        String sql = "delete from equipment_items where viking_id = ?";
        return jdbcTemplate.update(sql, vikingId);
    }
}
