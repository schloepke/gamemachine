
DROP TABLE IF EXISTS vitals;
CREATE TABLE vitals (
  id bigserial NOT NULL,

  vitals_character_id TEXT DEFAULT NULL,

  vitals_last_combat bigint DEFAULT NULL,

  vitals_dead integer DEFAULT NULL,

  vitals_entity_id TEXT DEFAULT NULL,

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

  vitals_zone_name TEXT DEFAULT NULL,

  vitals_id TEXT DEFAULT NULL,

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

  CONSTRAINT vitals_pkey PRIMARY KEY (id)
);
alter table vitals owner to gamemachine;
DROP TABLE IF EXISTS territories;
CREATE TABLE territories (
  id bigserial NOT NULL,

  territory_id TEXT NOT NULL,

  territory_owner TEXT NOT NULL,

  territory_keep TEXT DEFAULT NULL,

  CONSTRAINT territory_pkey PRIMARY KEY (id)
);
alter table territories owner to gamemachine;
DROP TABLE IF EXISTS guilds;
CREATE TABLE guilds (
  id bigserial NOT NULL,

  guild_id TEXT NOT NULL,

  guild_owner_id TEXT NOT NULL,

  guild_name TEXT DEFAULT NULL,

  CONSTRAINT guild_pkey PRIMARY KEY (id)
);
alter table guilds owner to gamemachine;
DROP TABLE IF EXISTS guild_members;
CREATE TABLE guild_members (
  id bigserial NOT NULL,

  guild_members_guild_id TEXT NOT NULL,

  guild_members_player_id TEXT NOT NULL,

  CONSTRAINT guild_members_pkey PRIMARY KEY (id)
);
alter table guild_members owner to gamemachine;
DROP TABLE IF EXISTS guild_actions;
CREATE TABLE guild_actions (
  id bigserial NOT NULL,

  guild_action_action TEXT NOT NULL,

  guild_action_to TEXT DEFAULT NULL,

  guild_action_from TEXT DEFAULT NULL,

  guild_action_response TEXT DEFAULT NULL,

  guild_action_guild_id TEXT DEFAULT NULL,

  guild_action_invite_id TEXT DEFAULT NULL,

  guild_action_guild_name TEXT DEFAULT NULL,

  CONSTRAINT guild_action_pkey PRIMARY KEY (id)
);
alter table guild_actions owner to gamemachine;
DROP TABLE IF EXISTS world_objects;
CREATE TABLE world_objects (
  id bigserial NOT NULL,

  world_object_player_item_id TEXT DEFAULT NULL,

  world_object_action integer DEFAULT NULL,

  world_object_id TEXT DEFAULT NULL,

  world_object_owner_id TEXT DEFAULT NULL,

  world_object_x integer DEFAULT NULL,

  world_object_y integer DEFAULT NULL,

  world_object_z integer DEFAULT NULL,

  world_object_rx integer DEFAULT NULL,

  world_object_ry integer DEFAULT NULL,

  world_object_rz integer DEFAULT NULL,

  world_object_rw integer DEFAULT NULL,

  world_object_max_health integer DEFAULT NULL,

  world_object_health integer DEFAULT NULL,

  world_object_parent_id TEXT DEFAULT NULL,

  world_object_destructable boolean DEFAULT NULL,

  world_object_prefab TEXT DEFAULT NULL,

  world_object_type integer DEFAULT NULL,

  world_object_grid TEXT DEFAULT NULL,

  world_object_current_user TEXT DEFAULT NULL,

  world_object_state integer DEFAULT NULL,

  CONSTRAINT world_object_pkey PRIMARY KEY (id)
);
alter table world_objects owner to gamemachine;
DROP TABLE IF EXISTS build_object_datas;
CREATE TABLE build_object_datas (
  id bigserial NOT NULL,

  build_object_datas_id TEXT NOT NULL,

  build_object_datas_data_text TEXT DEFAULT NULL,

  build_object_datas_character_id TEXT DEFAULT NULL,

  build_object_datas_zone TEXT DEFAULT NULL,

  build_object_datas_group integer DEFAULT NULL,

  CONSTRAINT build_object_datas_pkey PRIMARY KEY (id)
);
alter table build_object_datas owner to gamemachine;
DROP TABLE IF EXISTS buildable_areas;
CREATE TABLE buildable_areas (
  id bigserial NOT NULL,

  buildable_area_id TEXT NOT NULL,

  buildable_area_owner_id TEXT NOT NULL,

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

  CONSTRAINT buildable_area_pkey PRIMARY KEY (id)
);
alter table buildable_areas owner to gamemachine;
DROP TABLE IF EXISTS characters;
CREATE TABLE characters (
  id bigserial NOT NULL,

  character_id TEXT NOT NULL,

  character_uma_data TEXT DEFAULT NULL,

  character_player_id TEXT NOT NULL,

  character_part integer DEFAULT NULL,

  character_parts integer DEFAULT NULL,

  character_worldx integer DEFAULT NULL,

  character_worldy integer DEFAULT NULL,

  character_worldz integer DEFAULT NULL,

  character_include_uma_data boolean DEFAULT NULL,

  character_level integer DEFAULT NULL,

  character_vitals_template integer,

  character_game_entity_prefab TEXT DEFAULT NULL,

  character_region TEXT DEFAULT NULL,

  character_item_slot_data TEXT DEFAULT NULL,

     zone_zone_name TEXT DEFAULT NULL,
    
     zone_zone_number integer DEFAULT NULL,
    
     zone_zone_region TEXT DEFAULT NULL,
    
     zone_zone_hostname TEXT DEFAULT NULL,
    
     zone_zone_is_public boolean DEFAULT NULL,
    
     zone_zone_player_ids TEXT DEFAULT NULL,

  CONSTRAINT character_pkey PRIMARY KEY (id)
);
alter table characters owner to gamemachine;
DROP TABLE IF EXISTS players;
CREATE TABLE players (
  id bigserial NOT NULL,

  player_id TEXT NOT NULL,

  player_authenticated boolean DEFAULT NULL,

  player_authtoken integer DEFAULT NULL,

  player_password_hash TEXT DEFAULT NULL,

  player_game_id TEXT DEFAULT NULL,

  player_role integer,

  player_locked boolean DEFAULT NULL,

  player_ip integer DEFAULT NULL,

  player_ip_changed_at bigint DEFAULT NULL,

  player_character_id TEXT DEFAULT NULL,

  player_assigned_unity_instance boolean DEFAULT NULL,

  player_assigned boolean DEFAULT NULL,

  CONSTRAINT player_pkey PRIMARY KEY (id)
);
alter table players owner to gamemachine;
DROP TABLE IF EXISTS player_skills;
CREATE TABLE player_skills (
  id bigserial NOT NULL,

  player_skill_id TEXT NOT NULL,

  player_skill_icon_path TEXT DEFAULT NULL,

  player_skill_description TEXT DEFAULT NULL,

  player_skill_character_id TEXT NOT NULL,

  player_skill_status_effect_id TEXT DEFAULT NULL,

  player_skill_is_combo_part integer DEFAULT NULL,

  player_skill_is_passive integer DEFAULT NULL,

  player_skill_icon_uuid TEXT DEFAULT NULL,

  player_skill_status_effects TEXT DEFAULT NULL,

  player_skill_category integer,

  player_skill_range integer DEFAULT NULL,

  player_skill_weapon_type integer,

  player_skill_projectile_behavior integer,

  player_skill_target_circle boolean DEFAULT NULL,

  player_skill_max_level double precision DEFAULT NULL,

  player_skill_level double precision DEFAULT NULL,

     useEffect_unity_object_path TEXT DEFAULT NULL,
    
     useEffect_unity_object_uuid TEXT DEFAULT NULL,
    
     useEffect_unity_object_name TEXT DEFAULT NULL,

     icon_unity_object_path TEXT DEFAULT NULL,
    
     icon_unity_object_uuid TEXT DEFAULT NULL,
    
     icon_unity_object_name TEXT DEFAULT NULL,

     useSound_unity_object_path TEXT DEFAULT NULL,
    
     useSound_unity_object_uuid TEXT DEFAULT NULL,
    
     useSound_unity_object_name TEXT DEFAULT NULL,

     projectile_unity_object_path TEXT DEFAULT NULL,
    
     projectile_unity_object_uuid TEXT DEFAULT NULL,
    
     projectile_unity_object_name TEXT DEFAULT NULL,

     hitEffect_unity_object_path TEXT DEFAULT NULL,
    
     hitEffect_unity_object_uuid TEXT DEFAULT NULL,
    
     hitEffect_unity_object_name TEXT DEFAULT NULL,

  CONSTRAINT player_skill_pkey PRIMARY KEY (id)
);
alter table player_skills owner to gamemachine;
DROP TABLE IF EXISTS craftable_items;
CREATE TABLE craftable_items (
  id bigserial NOT NULL,

  craftable_item_id TEXT NOT NULL,

  craftable_item_item1 TEXT DEFAULT NULL,

  craftable_item_item1_quantity integer DEFAULT NULL,

  craftable_item_item2 TEXT DEFAULT NULL,

  craftable_item_item2_quantity integer DEFAULT NULL,

  craftable_item_item3 TEXT DEFAULT NULL,

  craftable_item_item3_quantity integer DEFAULT NULL,

  craftable_item_item4 TEXT DEFAULT NULL,

  craftable_item_item4_quantity integer DEFAULT NULL,

  CONSTRAINT craftable_item_pkey PRIMARY KEY (id)
);
alter table craftable_items owner to gamemachine;
DROP TABLE IF EXISTS player_items;
CREATE TABLE player_items (
  id bigserial NOT NULL,

  player_item_id TEXT NOT NULL,

  player_item_name TEXT NOT NULL,

  player_item_quantity integer NOT NULL,

  player_item_color TEXT DEFAULT NULL,

  player_item_weapon boolean DEFAULT NULL,

  player_item_player_id TEXT DEFAULT NULL,

  player_item_harvestable integer DEFAULT NULL,

  player_item_crafting_resource integer DEFAULT NULL,

  player_item_craftable integer DEFAULT NULL,

  player_item_is_consumable boolean DEFAULT NULL,

  player_item_type integer DEFAULT NULL,

  player_item_max_health integer DEFAULT NULL,

  player_item_health integer DEFAULT NULL,

  player_item_level integer DEFAULT NULL,

  player_item_character_id TEXT DEFAULT NULL,

  player_item_container_id TEXT DEFAULT NULL,

  player_item_updated_at integer DEFAULT NULL,

  player_item_location_id TEXT DEFAULT NULL,

  player_item_slot_count integer DEFAULT NULL,

  player_item_stackable boolean DEFAULT NULL,

  player_item_location_type TEXT DEFAULT NULL,

  player_item_stack_max integer DEFAULT NULL,

  player_item_container_slot integer DEFAULT NULL,

  player_item_icon_uuid TEXT DEFAULT NULL,

  player_item_icon_path TEXT DEFAULT NULL,

  player_item_reference_id TEXT DEFAULT NULL,

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
    
     cost_cost_currency TEXT DEFAULT NULL,

     icon_unity_object_path TEXT DEFAULT NULL,
    
     icon_unity_object_uuid TEXT DEFAULT NULL,
    
     icon_unity_object_name TEXT DEFAULT NULL,

  CONSTRAINT player_item_pkey PRIMARY KEY (id)
);
alter table player_items owner to gamemachine;
DROP TABLE IF EXISTS test_objects;
CREATE TABLE test_objects (
  id bigserial NOT NULL,

  test_object_optional_string TEXT DEFAULT NULL,

  test_object_required_string TEXT NOT NULL,

  test_object_numbers integer DEFAULT NULL,

  test_object_bstring bytea DEFAULT NULL,

  test_object_bvalue boolean DEFAULT NULL,

  test_object_dvalue double precision DEFAULT NULL,

  test_object_fvalue double precision DEFAULT NULL,

  test_object_numbers64 bigint DEFAULT NULL,

  test_object_id TEXT NOT NULL,

  CONSTRAINT test_object_pkey PRIMARY KEY (id)
);
alter table test_objects owner to gamemachine;
DROP TABLE IF EXISTS region_infos;
CREATE TABLE region_infos (
  id bigserial NOT NULL,

  region_info_id TEXT NOT NULL,

  region_info_node TEXT DEFAULT NULL,

  region_info_assigned boolean DEFAULT NULL,

  region_info_number integer DEFAULT NULL,

  region_info_hostname TEXT DEFAULT NULL,

  CONSTRAINT region_info_pkey PRIMARY KEY (id)
);
alter table region_infos owner to gamemachine;