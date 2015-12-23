
CREATE TABLE `vitals` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `vitals_character_id` varchar(1024) DEFAULT NULL,

  `vitals_last_combat` bigint DEFAULT NULL,

  `vitals_dead` int(11) DEFAULT NULL,

  `vitals_entity_id` varchar(1024) DEFAULT NULL,

  `vitals_type` int(11),

  `vitals_spell_resist` int(11) DEFAULT NULL,

  `vitals_elemental_resist` int(11) DEFAULT NULL,

  `vitals_spell_penetration` int(11) DEFAULT NULL,

  `vitals_magic_regen` int(11) DEFAULT NULL,

  `vitals_health_regen` int(11) DEFAULT NULL,

  `vitals_stamina_regen` int(11) DEFAULT NULL,

  `vitals_armor` int(11) DEFAULT NULL,

  `vitals_magic` int(11) DEFAULT NULL,

  `vitals_health` int(11) DEFAULT NULL,

  `vitals_stamina` int(11) DEFAULT NULL,

  `vitals_movement_speed` int(11) DEFAULT NULL,

  `vitals_ability_speed` int(11) DEFAULT NULL,

  `vitals_template` int(11),

  `vitals_is_base_vitals` tinyint(4) DEFAULT NULL,

  `vitals_combat_regen_mod` float DEFAULT NULL,

  `vitals_in_combat` tinyint(4) DEFAULT NULL,

  `vitals_zone_name` varchar(1024) DEFAULT NULL,

  `vitals_id` varchar(1024) DEFAULT NULL,

  `vitals_spell_resist_max` int(11) DEFAULT NULL,

  `vitals_elemental_resist_max` int(11) DEFAULT NULL,

  `vitals_spell_penetration_max` int(11) DEFAULT NULL,

  `vitals_magic_regen_max` int(11) DEFAULT NULL,

  `vitals_health_regen_max` int(11) DEFAULT NULL,

  `vitals_stamina_regen_max` int(11) DEFAULT NULL,

  `vitals_armor_max` int(11) DEFAULT NULL,

  `vitals_magic_max` int(11) DEFAULT NULL,

  `vitals_health_max` int(11) DEFAULT NULL,

  `vitals_stamina_max` int(11) DEFAULT NULL,

  `vitals_movement_speed_max` int(11) DEFAULT NULL,

  `vitals_ability_speed_max` int(11) DEFAULT NULL,

  `vitals_update_id` int(11) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `territories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `territory_id` varchar(1024) NOT NULL,

  `territory_owner` varchar(1024) NOT NULL,

  `territory_keep` varchar(1024) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `guilds` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `guild_id` varchar(1024) NOT NULL,

  `guild_owner_id` varchar(1024) NOT NULL,

  `guild_name` varchar(1024) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `guild_members` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `guild_members_guild_id` varchar(1024) NOT NULL,

  `guild_members_player_id` varchar(1024) NOT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `guild_actions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `guild_action_action` varchar(1024) NOT NULL,

  `guild_action_to` varchar(1024) DEFAULT NULL,

  `guild_action_from` varchar(1024) DEFAULT NULL,

  `guild_action_response` varchar(1024) DEFAULT NULL,

  `guild_action_guild_id` varchar(1024) DEFAULT NULL,

  `guild_action_invite_id` varchar(1024) DEFAULT NULL,

  `guild_action_guild_name` varchar(1024) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `world_objects` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `world_object_player_item_id` varchar(1024) DEFAULT NULL,

  `world_object_action` int(11) DEFAULT NULL,

  `world_object_id` varchar(1024) DEFAULT NULL,

  `world_object_owner_id` varchar(1024) DEFAULT NULL,

  `world_object_x` int(11) DEFAULT NULL,

  `world_object_y` int(11) DEFAULT NULL,

  `world_object_z` int(11) DEFAULT NULL,

  `world_object_rx` int(11) DEFAULT NULL,

  `world_object_ry` int(11) DEFAULT NULL,

  `world_object_rz` int(11) DEFAULT NULL,

  `world_object_rw` int(11) DEFAULT NULL,

  `world_object_max_health` int(11) DEFAULT NULL,

  `world_object_health` int(11) DEFAULT NULL,

  `world_object_parent_id` varchar(1024) DEFAULT NULL,

  `world_object_destructable` tinyint(4) DEFAULT NULL,

  `world_object_prefab` varchar(1024) DEFAULT NULL,

  `world_object_type` int(11) DEFAULT NULL,

  `world_object_grid` varchar(1024) DEFAULT NULL,

  `world_object_current_user` varchar(1024) DEFAULT NULL,

  `world_object_state` int(11) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `build_object_datas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `build_object_datas_id` varchar(1024) NOT NULL,

  `build_object_datas_data` varchar(1024) DEFAULT NULL,

  `build_object_datas_character_id` varchar(1024) DEFAULT NULL,

  `build_object_datas_zone` varchar(1024) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `buildable_areas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `buildable_area_id` varchar(1024) NOT NULL,

  `buildable_area_owner_id` varchar(1024) NOT NULL,

      size_`gm_vector3_x` float DEFAULT NULL,
    
      size_`gm_vector3_y` float DEFAULT NULL,
    
      size_`gm_vector3_z` float DEFAULT NULL,
    
      size_`gm_vector3_xi` int(11) DEFAULT NULL,
    
      size_`gm_vector3_yi` int(11) DEFAULT NULL,
    
      size_`gm_vector3_zi` int(11) DEFAULT NULL,
    
      size_`gm_vector3_vertice` int(11) DEFAULT NULL,

      position_`gm_vector3_x` float DEFAULT NULL,
    
      position_`gm_vector3_y` float DEFAULT NULL,
    
      position_`gm_vector3_z` float DEFAULT NULL,
    
      position_`gm_vector3_xi` int(11) DEFAULT NULL,
    
      position_`gm_vector3_yi` int(11) DEFAULT NULL,
    
      position_`gm_vector3_zi` int(11) DEFAULT NULL,
    
      position_`gm_vector3_vertice` int(11) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `characters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `character_id` varchar(1024) NOT NULL,

  `character_uma_data` varchar(1024) DEFAULT NULL,

  `character_player_id` varchar(1024) NOT NULL,

  `character_part` int(11) DEFAULT NULL,

  `character_parts` int(11) DEFAULT NULL,

  `character_worldx` int(11) DEFAULT NULL,

  `character_worldy` int(11) DEFAULT NULL,

  `character_worldz` int(11) DEFAULT NULL,

  `character_include_uma_data` tinyint(4) DEFAULT NULL,

  `character_level` int(11) DEFAULT NULL,

  `character_vitals_template` int(11),

  `character_game_entity_prefab` varchar(1024) DEFAULT NULL,

  `character_region` varchar(1024) DEFAULT NULL,

  `character_item_slot_data` varchar(1024) DEFAULT NULL,

      zone_`zone_name` varchar(1024) DEFAULT NULL,
    
      zone_`zone_number` int(11) DEFAULT NULL,
    
      zone_`zone_region` varchar(1024) DEFAULT NULL,
    
      zone_`zone_hostname` varchar(1024) DEFAULT NULL,
    
      zone_`zone_is_public` tinyint(4) DEFAULT NULL,
    
      zone_`zone_player_ids` varchar(1024) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `players` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `player_id` varchar(1024) NOT NULL,

  `player_authenticated` tinyint(4) DEFAULT NULL,

  `player_authtoken` int(11) DEFAULT NULL,

  `player_password_hash` varchar(1024) DEFAULT NULL,

  `player_game_id` varchar(1024) DEFAULT NULL,

  `player_role` int(11),

  `player_locked` tinyint(4) DEFAULT NULL,

  `player_ip` int(11) DEFAULT NULL,

  `player_ip_changed_at` bigint DEFAULT NULL,

  `player_character_id` varchar(1024) DEFAULT NULL,

  `player_assigned_unity_instance` tinyint(4) DEFAULT NULL,

  `player_assigned` tinyint(4) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `player_skills` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `player_skill_id` varchar(1024) NOT NULL,

  `player_skill_icon_path` varchar(1024) DEFAULT NULL,

  `player_skill_description` varchar(1024) DEFAULT NULL,

  `player_skill_character_id` varchar(1024) NOT NULL,

  `player_skill_status_effect_id` varchar(1024) DEFAULT NULL,

  `player_skill_is_combo_part` int(11) DEFAULT NULL,

  `player_skill_is_passive` int(11) DEFAULT NULL,

  `player_skill_icon_uuid` varchar(1024) DEFAULT NULL,

  `player_skill_status_effects` varchar(1024) DEFAULT NULL,

  `player_skill_category` int(11),

  `player_skill_range` int(11) DEFAULT NULL,

  `player_skill_weapon_type` int(11),

  `player_skill_projectile_behavior` int(11),

  `player_skill_target_circle` tinyint(4) DEFAULT NULL,

  `player_skill_max_level` float DEFAULT NULL,

  `player_skill_level` float DEFAULT NULL,

      useEffect_`unity_object_path` varchar(1024) DEFAULT NULL,
    
      useEffect_`unity_object_uuid` varchar(1024) DEFAULT NULL,
    
      useEffect_`unity_object_name` varchar(1024) DEFAULT NULL,

      icon_`unity_object_path` varchar(1024) DEFAULT NULL,
    
      icon_`unity_object_uuid` varchar(1024) DEFAULT NULL,
    
      icon_`unity_object_name` varchar(1024) DEFAULT NULL,

      useSound_`unity_object_path` varchar(1024) DEFAULT NULL,
    
      useSound_`unity_object_uuid` varchar(1024) DEFAULT NULL,
    
      useSound_`unity_object_name` varchar(1024) DEFAULT NULL,

      projectile_`unity_object_path` varchar(1024) DEFAULT NULL,
    
      projectile_`unity_object_uuid` varchar(1024) DEFAULT NULL,
    
      projectile_`unity_object_name` varchar(1024) DEFAULT NULL,

      hitEffect_`unity_object_path` varchar(1024) DEFAULT NULL,
    
      hitEffect_`unity_object_uuid` varchar(1024) DEFAULT NULL,
    
      hitEffect_`unity_object_name` varchar(1024) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `craftable_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `craftable_item_id` varchar(1024) NOT NULL,

  `craftable_item_item1` varchar(1024) DEFAULT NULL,

  `craftable_item_item1_quantity` int(11) DEFAULT NULL,

  `craftable_item_item2` varchar(1024) DEFAULT NULL,

  `craftable_item_item2_quantity` int(11) DEFAULT NULL,

  `craftable_item_item3` varchar(1024) DEFAULT NULL,

  `craftable_item_item3_quantity` int(11) DEFAULT NULL,

  `craftable_item_item4` varchar(1024) DEFAULT NULL,

  `craftable_item_item4_quantity` int(11) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `player_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `player_item_id` varchar(1024) NOT NULL,

  `player_item_name` varchar(1024) NOT NULL,

  `player_item_quantity` int(11) NOT NULL,

  `player_item_color` varchar(1024) DEFAULT NULL,

  `player_item_weapon` tinyint(4) DEFAULT NULL,

  `player_item_player_id` varchar(1024) DEFAULT NULL,

  `player_item_harvestable` int(11) DEFAULT NULL,

  `player_item_crafting_resource` int(11) DEFAULT NULL,

  `player_item_craftable` int(11) DEFAULT NULL,

  `player_item_is_consumable` tinyint(4) DEFAULT NULL,

  `player_item_type` int(11) DEFAULT NULL,

  `player_item_max_health` int(11) DEFAULT NULL,

  `player_item_health` int(11) DEFAULT NULL,

  `player_item_level` int(11) DEFAULT NULL,

  `player_item_character_id` varchar(1024) DEFAULT NULL,

  `player_item_container_id` varchar(1024) DEFAULT NULL,

  `player_item_updated_at` int(11) DEFAULT NULL,

  `player_item_location_id` varchar(1024) DEFAULT NULL,

  `player_item_slot_count` int(11) DEFAULT NULL,

  `player_item_stackable` tinyint(4) DEFAULT NULL,

  `player_item_location_type` varchar(1024) DEFAULT NULL,

  `player_item_stack_max` int(11) DEFAULT NULL,

  `player_item_container_slot` int(11) DEFAULT NULL,

  `player_item_icon_uuid` varchar(1024) DEFAULT NULL,

  `player_item_icon_path` varchar(1024) DEFAULT NULL,

  `player_item_reference_id` varchar(1024) DEFAULT NULL,

  `player_item_hidden` tinyint(4) NOT NULL,

  `player_item_max_quantity` int(11) DEFAULT NULL,

  `player_item_active` tinyint(4) NOT NULL,

  `player_item_weight` float NOT NULL,

  `player_item_template_block_id` int(11) DEFAULT NULL,

  `player_item_category` int(11),

  `player_item_equipped` tinyint(4) DEFAULT NULL,

  `player_item_slot` int(11),

  `player_item_weapon_type` int(11),

      cost_`cost_amount` float DEFAULT NULL,
    
      cost_`cost_currency` varchar(1024) DEFAULT NULL,

      icon_`unity_object_path` varchar(1024) DEFAULT NULL,
    
      icon_`unity_object_uuid` varchar(1024) DEFAULT NULL,
    
      icon_`unity_object_name` varchar(1024) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `test_objects` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `test_object_optional_string` varchar(1024) DEFAULT NULL,

  `test_object_required_string` varchar(1024) NOT NULL,

  `test_object_numbers` int(11) DEFAULT NULL,

  `test_object_bstring` varbinary(2048) DEFAULT NULL,

  `test_object_bvalue` tinyint(4) DEFAULT NULL,

  `test_object_dvalue` double DEFAULT NULL,

  `test_object_fvalue` float DEFAULT NULL,

  `test_object_numbers64` bigint DEFAULT NULL,

  `test_object_id` varchar(1024) NOT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `region_infos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,

  `region_info_id` varchar(1024) NOT NULL,

  `region_info_node` varchar(1024) DEFAULT NULL,

  `region_info_assigned` tinyint(4) DEFAULT NULL,

  `region_info_number` int(11) DEFAULT NULL,

  `region_info_hostname` varchar(1024) DEFAULT NULL,

  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;