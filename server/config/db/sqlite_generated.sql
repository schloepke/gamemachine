
CREATE TABLE boats (

  boat_id varchar(1024) DEFAULT NULL,

  boat_zone varchar(1024) DEFAULT NULL,

  boat_x integer DEFAULT NULL,

  boat_y integer DEFAULT NULL,

  boat_z integer DEFAULT NULL,

  boat_player_item_id varchar(1024) DEFAULT NULL,

  boat_owner_id varchar(1024) DEFAULT NULL,

  boat_heading integer DEFAULT NULL,

  boat_dock_id varchar(1024) DEFAULT NULL,

  boat_state integer,

  boat_captain_id varchar(1024) DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE vitals (

  vitals_character_id varchar(1024) DEFAULT NULL,

  vitals_last_combat integer DEFAULT NULL,

  vitals_dead integer DEFAULT NULL,

  vitals_entity_id varchar(1024) DEFAULT NULL,

  vitals_type integer,

  vitals_spell_resist integer DEFAULT NULL,

  vitals_elemental_resist integer DEFAULT NULL,

  vitals_spell_penetration integer DEFAULT NULL,

  vitals_magic_regen integer DEFAULT NULL,

  vitals_health_regen integer DEFAULT NULL,

  vitals_stamina_regen integer DEFAULT NULL,

  vitals_armor integer DEFAULT NULL,

  vitals_magic integer DEFAULT NULL,

  vitals_health integer DEFAULT NULL,

  vitals_stamina integer DEFAULT NULL,

  vitals_movement_speed integer DEFAULT NULL,

  vitals_ability_speed integer DEFAULT NULL,

  vitals_template integer,

  vitals_is_base_vitals boolean DEFAULT NULL,

  vitals_combat_regen_mod double precision DEFAULT NULL,

  vitals_in_combat boolean DEFAULT NULL,

  vitals_zone_name varchar(1024) DEFAULT NULL,

  vitals_id varchar(1024) DEFAULT NULL,

  vitals_spell_resist_max integer DEFAULT NULL,

  vitals_elemental_resist_max integer DEFAULT NULL,

  vitals_spell_penetration_max integer DEFAULT NULL,

  vitals_magic_regen_max integer DEFAULT NULL,

  vitals_health_regen_max integer DEFAULT NULL,

  vitals_stamina_regen_max integer DEFAULT NULL,

  vitals_armor_max integer DEFAULT NULL,

  vitals_magic_max integer DEFAULT NULL,

  vitals_health_max integer DEFAULT NULL,

  vitals_stamina_max integer DEFAULT NULL,

  vitals_movement_speed_max integer DEFAULT NULL,

  vitals_ability_speed_max integer DEFAULT NULL,

  vitals_update_id integer DEFAULT NULL,

  vitals_death_time double precision DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE territories (

  territory_id varchar(1024) NOT NULL,

  territory_owner varchar(1024) NOT NULL,

  territory_keep varchar(1024) DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE guilds (

  guild_id varchar(1024) NOT NULL,

  guild_owner_id varchar(1024) NOT NULL,

  guild_name varchar(1024) DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE guild_invites (

  guild_invite_id varchar(1024) NOT NULL,

  guild_invite_to varchar(1024) NOT NULL,

  guild_invite_from varchar(1024) NOT NULL,

  guild_invite_guild_id varchar(1024) DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE world_objects (

  world_object_player_item_id varchar(1024) DEFAULT NULL,

  world_object_action integer DEFAULT NULL,

  world_object_id varchar(1024) DEFAULT NULL,

  world_object_owner_id varchar(1024) DEFAULT NULL,

  world_object_x integer DEFAULT NULL,

  world_object_y integer DEFAULT NULL,

  world_object_z integer DEFAULT NULL,

  world_object_rx integer DEFAULT NULL,

  world_object_ry integer DEFAULT NULL,

  world_object_rz integer DEFAULT NULL,

  world_object_rw integer DEFAULT NULL,

  world_object_max_health integer DEFAULT NULL,

  world_object_health integer DEFAULT NULL,

  world_object_parent_id varchar(1024) DEFAULT NULL,

  world_object_destructable boolean DEFAULT NULL,

  world_object_prefab varchar(1024) DEFAULT NULL,

  world_object_type integer DEFAULT NULL,

  world_object_grid varchar(1024) DEFAULT NULL,

  world_object_current_user varchar(1024) DEFAULT NULL,

  world_object_state integer DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE build_object_datas (

  build_object_datas_id varchar(1024) NOT NULL,

  build_object_datas_data_text varchar(1024) DEFAULT NULL,

  build_object_datas_character_id varchar(1024) DEFAULT NULL,

  build_object_datas_zone varchar(1024) DEFAULT NULL,

  build_object_datas_group integer DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE buildable_areas (

  buildable_area_id varchar(1024) NOT NULL,

  buildable_area_owner_id varchar(1024) NOT NULL,

      size_gm_vector3_x double precision DEFAULT NULL,
    
      size_gm_vector3_y double precision DEFAULT NULL,
    
      size_gm_vector3_z double precision DEFAULT NULL,
    
      size_gm_vector3_xi integer DEFAULT NULL,
    
      size_gm_vector3_yi integer DEFAULT NULL,
    
      size_gm_vector3_zi integer DEFAULT NULL,
    
      size_gm_vector3_vertice integer DEFAULT NULL,

      position_gm_vector3_x double precision DEFAULT NULL,
    
      position_gm_vector3_y double precision DEFAULT NULL,
    
      position_gm_vector3_z double precision DEFAULT NULL,
    
      position_gm_vector3_xi integer DEFAULT NULL,
    
      position_gm_vector3_yi integer DEFAULT NULL,
    
      position_gm_vector3_zi integer DEFAULT NULL,
    
      position_gm_vector3_vertice integer DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE characters (

  character_id varchar(1024) NOT NULL,

  character_uma_data varchar(1024) DEFAULT NULL,

  character_player_id varchar(1024) NOT NULL,

  character_part integer DEFAULT NULL,

  character_parts integer DEFAULT NULL,

  character_worldx integer DEFAULT NULL,

  character_worldy integer DEFAULT NULL,

  character_worldz integer DEFAULT NULL,

  character_include_uma_data boolean DEFAULT NULL,

  character_level integer DEFAULT NULL,

  character_vitals_template integer,

  character_game_entity_prefab varchar(1024) DEFAULT NULL,

  character_region varchar(1024) DEFAULT NULL,

  character_item_slot_data varchar(1024) DEFAULT NULL,

  character_guild_id varchar(1024) DEFAULT NULL,

  character_bind_point varchar(1024) DEFAULT NULL,

  character_faction integer,

  character_owner_id varchar(1024) DEFAULT NULL,

  character_first_name varchar(1024) DEFAULT NULL,

  character_last_name varchar(1024) DEFAULT NULL,

      zone_zone_name varchar(1024) DEFAULT NULL,
    
      zone_zone_number integer DEFAULT NULL,
    
      zone_zone_region varchar(1024) DEFAULT NULL,
    
      zone_zone_hostname varchar(1024) DEFAULT NULL,
    
      zone_zone_is_public boolean DEFAULT NULL,
    
      zone_zone_player_ids varchar(1024) DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE faction_standings (

  faction_standing_me integer,

  faction_standing_them integer,

  faction_standing_standing double precision DEFAULT NULL,

  faction_standing_id varchar(1024) DEFAULT NULL,

  faction_standing_me_character_id varchar(1024) DEFAULT NULL,

  faction_standing_them_character_id varchar(1024) DEFAULT NULL,

  faction_standing_type integer,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE players (

  player_id varchar(1024) NOT NULL,

  player_authenticated boolean DEFAULT NULL,

  player_authtoken integer DEFAULT NULL,

  player_password_hash varchar(1024) DEFAULT NULL,

  player_game_id varchar(1024) DEFAULT NULL,

  player_role integer,

  player_locked boolean DEFAULT NULL,

  player_ip integer DEFAULT NULL,

  player_ip_changed_at integer DEFAULT NULL,

  player_character_id varchar(1024) DEFAULT NULL,

  player_assigned_unity_instance boolean DEFAULT NULL,

  player_assigned boolean DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE player_skills (

  player_skill_id varchar(1024) NOT NULL,

  player_skill_icon_path varchar(1024) DEFAULT NULL,

  player_skill_description varchar(1024) DEFAULT NULL,

  player_skill_character_id varchar(1024) NOT NULL,

  player_skill_status_effect_id varchar(1024) DEFAULT NULL,

  player_skill_is_combo_part integer DEFAULT NULL,

  player_skill_is_passive integer DEFAULT NULL,

  player_skill_icon_uuid varchar(1024) DEFAULT NULL,

  player_skill_status_effects varchar(1024) DEFAULT NULL,

  player_skill_category integer,

  player_skill_range integer DEFAULT NULL,

  player_skill_weapon_type integer,

  player_skill_projectile_behavior integer,

  player_skill_target_circle boolean DEFAULT NULL,

  player_skill_max_level double precision DEFAULT NULL,

  player_skill_level double precision DEFAULT NULL,

  player_skill_delay double precision DEFAULT NULL,

      useEffect_unity_object_path varchar(1024) DEFAULT NULL,
    
      useEffect_unity_object_uuid varchar(1024) DEFAULT NULL,
    
      useEffect_unity_object_name varchar(1024) DEFAULT NULL,

      icon_unity_object_path varchar(1024) DEFAULT NULL,
    
      icon_unity_object_uuid varchar(1024) DEFAULT NULL,
    
      icon_unity_object_name varchar(1024) DEFAULT NULL,

      useSound_unity_object_path varchar(1024) DEFAULT NULL,
    
      useSound_unity_object_uuid varchar(1024) DEFAULT NULL,
    
      useSound_unity_object_name varchar(1024) DEFAULT NULL,

      projectile_unity_object_path varchar(1024) DEFAULT NULL,
    
      projectile_unity_object_uuid varchar(1024) DEFAULT NULL,
    
      projectile_unity_object_name varchar(1024) DEFAULT NULL,

      hitEffect_unity_object_path varchar(1024) DEFAULT NULL,
    
      hitEffect_unity_object_uuid varchar(1024) DEFAULT NULL,
    
      hitEffect_unity_object_name varchar(1024) DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE craftable_items (

  craftable_item_id varchar(1024) NOT NULL,

  craftable_item_item1 varchar(1024) DEFAULT NULL,

  craftable_item_item1_quantity integer DEFAULT NULL,

  craftable_item_item2 varchar(1024) DEFAULT NULL,

  craftable_item_item2_quantity integer DEFAULT NULL,

  craftable_item_item3 varchar(1024) DEFAULT NULL,

  craftable_item_item3_quantity integer DEFAULT NULL,

  craftable_item_item4 varchar(1024) DEFAULT NULL,

  craftable_item_item4_quantity integer DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE player_items (

  player_item_id varchar(1024) NOT NULL,

  player_item_name varchar(1024) NOT NULL,

  player_item_quantity integer NOT NULL,

  player_item_color varchar(1024) DEFAULT NULL,

  player_item_weapon boolean DEFAULT NULL,

  player_item_player_id varchar(1024) DEFAULT NULL,

  player_item_harvestable integer DEFAULT NULL,

  player_item_crafting_resource integer DEFAULT NULL,

  player_item_craftable integer DEFAULT NULL,

  player_item_is_consumable boolean DEFAULT NULL,

  player_item_type integer DEFAULT NULL,

  player_item_max_health integer DEFAULT NULL,

  player_item_health integer DEFAULT NULL,

  player_item_level integer DEFAULT NULL,

  player_item_character_id varchar(1024) DEFAULT NULL,

  player_item_container_id varchar(1024) DEFAULT NULL,

  player_item_updated_at integer DEFAULT NULL,

  player_item_location_id varchar(1024) DEFAULT NULL,

  player_item_slot_count integer DEFAULT NULL,

  player_item_stackable boolean DEFAULT NULL,

  player_item_location_type varchar(1024) DEFAULT NULL,

  player_item_stack_max integer DEFAULT NULL,

  player_item_container_slot integer DEFAULT NULL,

  player_item_icon_uuid varchar(1024) DEFAULT NULL,

  player_item_icon_path varchar(1024) DEFAULT NULL,

  player_item_reference_id varchar(1024) DEFAULT NULL,

  player_item_hidden boolean NOT NULL,

  player_item_max_quantity integer DEFAULT NULL,

  player_item_active boolean NOT NULL,

  player_item_weight double precision NOT NULL,

  player_item_template_block_id integer DEFAULT NULL,

  player_item_category integer,

  player_item_equipped boolean DEFAULT NULL,

  player_item_slot integer,

  player_item_weapon_type integer,

      cost_cost_amount double precision DEFAULT NULL,
    
      cost_cost_currency varchar(1024) DEFAULT NULL,

      icon_unity_object_path varchar(1024) DEFAULT NULL,
    
      icon_unity_object_uuid varchar(1024) DEFAULT NULL,
    
      icon_unity_object_name varchar(1024) DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE test_objects (

  test_object_optional_string varchar(1024) DEFAULT NULL,

  test_object_required_string varchar(1024) NOT NULL,

  test_object_numbers integer DEFAULT NULL,

  test_object_bstring blob DEFAULT NULL,

  test_object_bvalue boolean DEFAULT NULL,

  test_object_dvalue double precision DEFAULT NULL,

  test_object_fvalue double precision DEFAULT NULL,

  test_object_numbers64 integer DEFAULT NULL,

  test_object_id varchar(1024) NOT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);
CREATE TABLE region_infos (

  region_info_id varchar(1024) NOT NULL,

  region_info_node varchar(1024) DEFAULT NULL,

  region_info_assigned boolean DEFAULT NULL,

  region_info_number integer DEFAULT NULL,

  region_info_hostname varchar(1024) DEFAULT NULL,

id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
);