# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20120724203138) do

  create_table "data_editors", :force => true do |t|
    t.text     "data"
    t.string   "name"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
  end

  create_table "data_merges", :force => true do |t|
    t.text     "data"
    t.string   "strategy"
    t.string   "source_db"
    t.string   "destination_db"
    t.string   "user_id"
    t.integer  "status",         :default => 0
    t.datetime "finished_at"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "databases", :force => true do |t|
    t.string   "name"
    t.text     "data"
    t.integer  "status",        :default => 0
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "assets_branch"
  end

  create_table "datasets", :force => true do |t|
    t.string   "name"
    t.string   "dbname"
    t.string   "collection_name"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "snapshot"
  end

  create_table "deployment_packages", :force => true do |t|
    t.string   "name"
    t.string   "env"
    t.string   "tag"
    t.string   "client_branch"
    t.string   "clientlib_branch"
    t.string   "server_branch"
    t.string   "assets_branch"
    t.string   "database"
    t.string   "package_type"
    t.integer  "status",           :default => 0
    t.text     "data"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_ab_tests", :force => true do |t|
    t.string   "name"
    t.string   "test_group"
    t.string   "model_name"
    t.string   "record"
    t.string   "column_name"
    t.string   "test_type"
    t.string   "value"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "identifier"
    t.boolean  "is_invalid"
  end

  create_table "game_action_levels", :force => true do |t|
    t.text     "action_id"
    t.string   "animation_id"
    t.integer  "level"
    t.decimal  "min_dmg",         :precision => 12, :scale => 6
    t.decimal  "max_dmg",         :precision => 12, :scale => 6
    t.integer  "hits"
    t.text     "status_id"
    t.decimal  "status_chance",   :precision => 12, :scale => 6
    t.decimal  "hit_chance",      :precision => 12, :scale => 6
    t.decimal  "critical",        :precision => 12, :scale => 6
    t.decimal  "critical_chance", :precision => 12, :scale => 6
    t.string   "action_type"
    t.string   "description"
    t.string   "cool_down_turns"
    t.string   "tags"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "name"
    t.string   "lock_id"
    t.string   "cost_id"
    t.decimal  "energy",          :precision => 12, :scale => 6
    t.string   "status_target"
    t.boolean  "start_cooled_up",                                :default => false
    t.decimal  "min_dmg_flat",    :precision => 12, :scale => 6
    t.decimal  "max_dmg_flat",    :precision => 12, :scale => 6
  end

  create_table "game_action_tags", :force => true do |t|
    t.string   "name"
    t.string   "disp_name"
    t.string   "status_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_actions", :force => true do |t|
    t.string   "name"
    t.string   "disp_name"
    t.string   "icon_asset_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "lock_id"
    t.integer  "sequence_number"
  end

  create_table "game_advisories", :force => true do |t|
    t.string   "name"
    t.string   "category"
    t.string   "advisor_id"
    t.string   "description"
    t.integer  "priority"
    t.string   "research_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_advisors", :force => true do |t|
    t.string   "name"
    t.string   "title"
    t.string   "icon_asset_id"
    t.string   "fallback_category"
    t.string   "fallback_desc"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "image_asset_id"
    t.string   "lock_toast_id"
    t.string   "cost_toast_id"
  end

  create_table "game_agent_ranks", :force => true do |t|
    t.integer  "team_level"
    t.string   "rank_name"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_ai_script_rules", :force => true do |t|
    t.string   "target_rule"
    t.string   "target_or_tags"
    t.string   "target_not_tags"
    t.string   "enemy_or_tags"
    t.string   "enemy_not_tags"
    t.integer  "round_num"
    t.decimal  "self_hp_min",        :precision => 12, :scale => 6
    t.decimal  "self_hp_max",        :precision => 12, :scale => 6
    t.decimal  "enemy_low_hp_min",   :precision => 12, :scale => 6
    t.decimal  "enemy_low_hp_max",   :precision => 12, :scale => 6
    t.decimal  "enemy_avg_hp_min",   :precision => 12, :scale => 6
    t.decimal  "enemy_avg_hp_max",   :precision => 12, :scale => 6
    t.decimal  "last_target_hp_min", :precision => 12, :scale => 6
    t.decimal  "last_target_hp_max", :precision => 12, :scale => 6
    t.decimal  "ally_low_hp_min",    :precision => 12, :scale => 6
    t.decimal  "ally_low_hp_max",    :precision => 12, :scale => 6
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "allies_alive"
    t.integer  "enemies_alive"
    t.string   "self_or_tags"
    t.string   "self_not_tags"
  end

  create_table "game_ai_scripts", :force => true do |t|
    t.string   "character_id"
    t.string   "combat_encounter_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "name"
    t.string   "action_type"
    t.string   "action_id"
    t.string   "ai_script_rule_id"
    t.integer  "priority"
    t.decimal  "weight",              :precision => 12, :scale => 6
    t.string   "combat_type"
  end

  create_table "game_assets", :force => true do |t|
    t.string   "name"
    t.string   "ref"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "sid",        :null => false
    t.string   "short_name"
    t.integer  "version"
  end

  add_index "game_assets", ["sid"], :name => "sid", :unique => true

  create_table "game_avatar_assets", :force => true do |t|
    t.string   "name"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "large_asset_id"
    t.string   "small_asset_id"
    t.string   "combat_asset_id"
    t.string   "gender"
    t.string   "x"
    t.string   "y"
    t.string   "combat_x"
    t.string   "combat_y"
    t.string   "item_id"
    t.string   "slot_id"
    t.string   "combat_muzzle_move"
    t.string   "combat_recoil_move"
    t.string   "flipped_combat_asset_id"
    t.string   "right_asset_id"
    t.string   "left_asset_id"
    t.string   "small_flipped_combat_asset_id"
  end

  create_table "game_boss_events", :force => true do |t|
    t.string   "villain_id",    :null => false
    t.string   "boss_type",     :null => false
    t.integer  "reveal_points"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "name"
  end

  add_index "game_boss_events", ["villain_id"], :name => "fk_game_boss_events_game_villains"

  create_table "game_buildings", :force => true do |t|
    t.string   "map_asset_id",    :null => false
    t.integer  "building_number", :null => false
    t.integer  "building_type",   :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "game_buildings", ["map_asset_id", "building_number"], :name => "map_building", :unique => true

  create_table "game_chapters", :force => true do |t|
    t.string   "name"
    t.string   "disp_name"
    t.text     "description",         :limit => 16777215
    t.integer  "sequence_number"
    t.string   "background_asset_id"
    t.string   "lock_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "complete_reward_id"
  end

  add_index "game_chapters", ["background_asset_id"], :name => "fk_game_chapters_game_assets_background"
  add_index "game_chapters", ["lock_id"], :name => "fk_game_chapters_game_locks"

  create_table "game_combat_backgrounds", :force => true do |t|
    t.string   "background_asset_id",                                :null => false
    t.integer  "building_number"
    t.decimal  "weight",              :precision => 12, :scale => 6
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "map_asset_id"
  end

  add_index "game_combat_backgrounds", ["background_asset_id"], :name => "fk_game_combat_backgrounds_game_assets_background"
  add_index "game_combat_backgrounds", ["map_asset_id"], :name => "fk_game_combat_backgrounds_game_assets_map"

  create_table "game_combat_encounters", :force => true do |t|
    t.string   "name"
    t.string   "wave1_villain1_villain_id"
    t.string   "wave1_villain2_villain_id"
    t.string   "wave1_villain3_villain_id"
    t.string   "wave2_villain1_villain_id"
    t.string   "wave2_villain2_villain_id"
    t.string   "wave2_villain3_villain_id"
    t.string   "wave3_villain1_villain_id"
    t.string   "wave3_villain2_villain_id"
    t.string   "wave3_villain3_villain_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_combat_events", :force => true do |t|
    t.integer  "threat_level"
    t.string   "combat_table_id"
    t.string   "lose_dialog_id"
    t.decimal  "in_combat_drop_rate_ko",  :precision => 12, :scale => 6
    t.string   "in_combat_loot_table_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "name"
    t.string   "cameo_hero_id"
    t.integer  "cameo_hero_level"
    t.boolean  "is_hard_cameo"
    t.decimal  "in_combat_drop_rate_hit", :precision => 12, :scale => 6
  end

  add_index "game_combat_events", ["cameo_hero_id"], :name => "fk_game_combat_events_game_heroes_cameo"
  add_index "game_combat_events", ["combat_table_id"], :name => "fk_game_combat_events_game_combat_tables_combat"
  add_index "game_combat_events", ["in_combat_loot_table_id"], :name => "fk_game_combat_events_game_loot_tables_in_combat"

  create_table "game_combat_letter_grades", :force => true do |t|
    t.string   "threat_level"
    t.integer  "number_of_enemies"
    t.integer  "c"
    t.integer  "b"
    t.integer  "a"
    t.integer  "s"
    t.integer  "s_plus"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_combat_table_encounters", :force => true do |t|
    t.string   "combat_table_id",                                                     :null => false
    t.string   "combat_encounter_id",                                                 :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
    t.decimal  "weight",              :precision => 12, :scale => 6, :default => 1.0
  end

  add_index "game_combat_table_encounters", ["combat_encounter_id"], :name => "fk_game_combat_table_encounters_game_combat_encounters"
  add_index "game_combat_table_encounters", ["combat_table_id"], :name => "fk_game_combat_table_encounters_game_combat_tables"

  create_table "game_combat_tables", :force => true do |t|
    t.string   "name"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_costs", :force => true do |t|
    t.string   "name"
    t.integer  "energy"
    t.integer  "soft_currency"
    t.integer  "hard_currency"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "item_collection_id"
    t.text     "details"
    t.integer  "challenge_points"
  end

  add_index "game_costs", ["item_collection_id"], :name => "fk_game_costs_game_item_collections"

  create_table "game_costs_collections", :force => true do |t|
    t.string   "item_collection_id", :null => false
    t.text     "cost_id",            :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_dialog_actions", :force => true do |t|
    t.string   "dialog_id"
    t.string   "label"
    t.string   "next_dialog_group_id"
    t.string   "next_dialog_sequence_num"
    t.string   "action"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "name"
  end

  create_table "game_dialog_groups", :force => true do |t|
    t.string   "name"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_dialogs", :force => true do |t|
    t.string   "dialog_group_id"
    t.string   "display_name"
    t.string   "layout"
    t.string   "arrow_id"
    t.boolean  "blocking"
    t.string   "image_asset_id"
    t.string   "message"
    t.string   "title"
    t.integer  "sequence_num"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "flip"
    t.string   "character_id"
    t.string   "name"
    t.string   "hero_id"
  end

  create_table "game_fd_leases", :force => true do |t|
    t.string   "name"
    t.string   "cost_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_fd_missions", :force => true do |t|
    t.string   "name"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_fd_planes", :force => true do |t|
    t.string   "name"
    t.string   "lock_id"
    t.string   "lock_toast_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "friend_cost"
  end

  create_table "game_flight_deck_aircrafts", :force => true do |t|
    t.string   "name"
    t.integer  "level"
    t.integer  "num_friends"
    t.string   "art_id"
    t.string   "takeoff_id"
    t.string   "landing_id"
    t.string   "icon_id"
    t.string   "cost_id"
    t.string   "cost_toast_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "takeoff_shadow_asset_id"
    t.string   "landing_shadow_asset_id"
    t.string   "disp_name"
    t.string   "lock_id"
  end

  add_index "game_flight_deck_aircrafts", ["lock_id"], :name => "fk_game_flight_deck_aircrafts_game_locks"

  create_table "game_flight_deck_map_slots", :force => true do |t|
    t.string   "flight_deck_map_id"
    t.string   "flight_deck_slot_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_flight_deck_maps", :force => true do |t|
    t.string   "name"
    t.string   "start_flight_deck_slot_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_flight_deck_slot_states", :force => true do |t|
    t.string   "flight_deck_slot_id"
    t.string   "consumable_id"
    t.string   "consumable_type"
    t.integer  "count"
    t.integer  "sequence_number"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_flight_deck_slots", :force => true do |t|
    t.string   "name"
    t.integer  "level"
    t.string   "lock_id"
    t.string   "lock_toast_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "next_slot_id"
    t.string   "slot_target"
    t.string   "finish_now_cost_id"
  end

  create_table "game_flight_deck_upgrades", :force => true do |t|
    t.string   "name"
    t.string   "description"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_gear_bonuses", :force => true do |t|
    t.integer  "level"
    t.float    "bonus"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_gears", :force => true do |t|
    t.string   "name"
    t.string   "action_id"
    t.integer  "action_level"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "game_gears", ["action_id"], :name => "fk_game_gears_game_actions"

  create_table "game_gifts", :force => true do |t|
    t.string   "icon_asset_id"
    t.string   "limited"
    t.string   "new_item"
    t.datetime "expires"
    t.string   "available"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "disp_name",        :null => false
    t.string   "description",      :null => false
    t.string   "collection_name"
    t.integer  "position"
    t.string   "locked_by"
    t.string   "reward_id",        :null => false
    t.string   "name",             :null => false
    t.integer  "short_id",         :null => false
    t.integer  "max_accept_limit"
  end

  create_table "game_hero_actions", :force => true do |t|
    t.text     "action_id",  :null => false
    t.integer  "level"
    t.text     "hero_id",    :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_hero_statuses", :force => true do |t|
    t.text     "status_id",  :null => false
    t.text     "hero_id",    :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_heroes", :force => true do |t|
    t.string   "name"
    t.string   "swf_asset_id"
    t.string   "icon_asset_id"
    t.string   "in_use"
    t.string   "state"
    t.string   "controller"
    t.integer  "xp"
    t.integer  "health_rating"
    t.integer  "stamina_rating"
    t.integer  "attack_rating"
    t.integer  "defense_rating"
    t.integer  "accuracy_rating"
    t.integer  "evasion_rating"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "default_counter"
    t.string   "tags"
    t.string   "purchase_lock_id"
    t.string   "cost_id"
    t.decimal  "ap_recharge_rate",   :precision => 12, :scale => 6
    t.decimal  "hp_recharge_rate",   :precision => 12, :scale => 6
    t.text     "description"
    t.string   "disp_name"
    t.text     "bio"
    t.integer  "initiative"
    t.string   "uniform_item_id"
    t.string   "large_asset_id"
    t.string   "team_tags"
    t.string   "rps_type"
    t.string   "obtain_dialog_id"
    t.integer  "sequence_number"
    t.string   "right_asset_id"
    t.string   "left_asset_id"
    t.string   "cog_name"
    t.string   "cog_image_asset_id"
    t.string   "cog_swf_asset_id"
  end

  add_index "game_heroes", ["large_asset_id"], :name => "fk_game_heroes_game_assets_large"
  add_index "game_heroes", ["obtain_dialog_id"], :name => "fk_game_heroes_game_dialog_groups_obtain"
  add_index "game_heroes", ["purchase_lock_id"], :name => "fk_game_heroes_game_locks_purchase"

  create_table "game_item_attributes", :force => true do |t|
    t.string   "name"
    t.string   "value"
    t.string   "item_id",    :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_item_collection_members", :force => true do |t|
    t.text     "item_collection_id", :null => false
    t.text     "item_id",            :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "qty"
  end

  create_table "game_item_collections", :force => true do |t|
    t.string   "name"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.text     "details"
  end

  create_table "game_items", :force => true do |t|
    t.string   "name"
    t.string   "disp_name"
    t.string   "cost_id"
    t.string   "item_type"
    t.string   "class_name"
    t.integer  "owned"
    t.string   "sell_reward_id"
    t.string   "icon_asset_id"
    t.string   "description"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "purchase_lock_id"
    t.string   "small_asset_id"
    t.string   "large_asset_id"
    t.string   "sort_order"
    t.string   "item_group"
    t.integer  "item_level"
    t.string   "store_tags"
    t.string   "use_reward_id"
    t.string   "status_id"
    t.decimal  "offensive_bonus",  :precision => 12, :scale => 6
    t.decimal  "defensive_bonus",  :precision => 12, :scale => 6
    t.integer  "q_level"
    t.integer  "start_date"
    t.integer  "expire_date"
    t.string   "highlight_text"
    t.boolean  "dynamic_qlevel"
  end

  add_index "game_items", ["sell_reward_id"], :name => "fk_game_items_game_rewards_sell"
  add_index "game_items", ["use_reward_id"], :name => "fk_game_items_game_rewards_use"

  create_table "game_locale_strings", :force => true do |t|
    t.string   "name"
    t.string   "locale"
    t.text     "text"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.boolean  "generated",  :default => false
  end

  add_index "game_locale_strings", ["locale"], :name => "patch_01_game_locale_string_locale_idx"
  add_index "game_locale_strings", ["name", "locale"], :name => "index_game_locale_strings_on_name_and_locale"
  add_index "game_locale_strings", ["name"], :name => "patch_01_game_locale_string_name_idx"

  create_table "game_lock_attributes", :force => true do |t|
    t.string   "lock_id",    :null => false
    t.string   "name"
    t.string   "value"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_locks", :force => true do |t|
    t.string   "name"
    t.string   "description"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.text     "details"
    t.string   "item_collection_id"
  end

  create_table "game_loot_events", :force => true do |t|
    t.integer  "deploy_length"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "name"
  end

  create_table "game_loot_table_rewards", :force => true do |t|
    t.string   "loot_table_id",                                      :null => false
    t.string   "reward_id"
    t.decimal  "weight",              :precision => 12, :scale => 6
    t.integer  "mastery_min"
    t.integer  "mastery_max"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "agent_level_min"
    t.integer  "agent_level_max"
    t.string   "child_loot_table_id"
  end

  add_index "game_loot_table_rewards", ["loot_table_id"], :name => "fk_game_loot_table_rewards_game_loot_tables"
  add_index "game_loot_table_rewards", ["reward_id"], :name => "fk_game_loot_table_rewards_game_rewards"

  create_table "game_loot_tables", :force => true do |t|
    t.string   "loot_type",  :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "name"
  end

  create_table "game_mission_events", :force => true do |t|
    t.string   "mission_id",                                                                                      :null => false
    t.integer  "sort_order"
    t.string   "required_hero_id"
    t.text     "fiction_text",              :limit => 16777215
    t.string   "fiction_asset_id"
    t.string   "reveal_dialog_id"
    t.string   "complete_dialog_id"
    t.string   "complete_loot_table_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "name"
    t.decimal  "complete_loot_drop_rate",                       :precision => 12, :scale => 6
    t.boolean  "is_starting_event",                                                            :default => false
    t.string   "required_hero_class"
    t.string   "start_dialog_id"
    t.string   "replay_start_dialog_id"
    t.string   "replay_reveal_dialog_id"
    t.string   "replay_complete_dialog_id"
    t.string   "reveal_color_shift"
  end

  add_index "game_mission_events", ["complete_loot_table_id"], :name => "fk_game_mission_events_game_loot_tables_loot"
  add_index "game_mission_events", ["fiction_asset_id"], :name => "fk_game_mission_events_game_assets_fiction"
  add_index "game_mission_events", ["mission_id"], :name => "fk_game_mission_events_game_missions"
  add_index "game_mission_events", ["required_hero_id"], :name => "fk_game_mission_events_game_heroes_required"
  add_index "game_mission_events", ["start_dialog_id"], :name => "fk_game_mission_events_game_dialog_groups_start"

  create_table "game_mission_locks", :force => true do |t|
    t.string   "lock_id",    :null => false
    t.string   "mission_id"
    t.integer  "mastery"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "game_mission_locks", ["lock_id"], :name => "fk_game_mission_locks_game_locks"
  add_index "game_mission_locks", ["mission_id"], :name => "fk_game_mission_locks_game_missions"

  create_table "game_mission_masteries", :force => true do |t|
    t.string   "mission_id",         :null => false
    t.integer  "level"
    t.integer  "score"
    t.integer  "enemy_level_min"
    t.integer  "enemy_level_max"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "complete_dialog_id"
    t.string   "reward_id"
    t.integer  "recommended_level"
  end

  add_index "game_mission_masteries", ["complete_dialog_id"], :name => "fk_game_mission_masteries_game_dialog_groups_complete"
  add_index "game_mission_masteries", ["mission_id"], :name => "fk_game_mission_masteries_game_missions"

  create_table "game_missions", :force => true do |t|
    t.string   "chapter_id",                                                                 :null => false
    t.string   "name"
    t.string   "disp_name"
    t.text     "description",             :limit => 16777215
    t.string   "coords"
    t.string   "destinations"
    t.string   "lock_id"
    t.string   "cost_id"
    t.string   "start_dialog_id"
    t.string   "complete_dialog_id"
    t.string   "map_asset_id"
    t.string   "image_asset_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "start_buildings"
    t.string   "mini_boss_buildings"
    t.string   "boss_buildings"
    t.string   "epic_boss_buildings"
    t.string   "event_buildings"
    t.boolean  "hide_when_locked"
    t.string   "unlock_dialog_id"
    t.integer  "sequence_number"
    t.integer  "base_score"
    t.decimal  "survival_multiplier_max",                     :precision => 12, :scale => 6
    t.integer  "survival_loss_par"
    t.string   "unlock_quest_id"
    t.text     "preview_text"
    t.string   "grand_reward_item_id"
    t.string   "gala_cost_epic_boss"
    t.string   "gala_cost_main_boss"
    t.string   "location"
    t.string   "color_shift"
  end

  add_index "game_missions", ["chapter_id"], :name => "fk_game_missions_game_chapters"
  add_index "game_missions", ["cost_id"], :name => "fk_game_missions_game_costs"
  add_index "game_missions", ["grand_reward_item_id"], :name => "fk_game_missions_game_items"
  add_index "game_missions", ["image_asset_id"], :name => "fk_game_missions_game_assets_image"
  add_index "game_missions", ["lock_id"], :name => "fk_game_missions_game_locks"
  add_index "game_missions", ["map_asset_id"], :name => "fk_game_missions_game_assets_map"
  add_index "game_missions", ["unlock_dialog_id"], :name => "fk_game_missions_game_dialog_groups_unlock"

  create_table "game_mods", :force => true do |t|
    t.string   "name"
    t.string   "mod_type"
    t.string   "value"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "op_type"
    t.string   "description"
  end

  create_table "game_naughty_words", :force => true do |t|
    t.string   "word"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_powerups", :force => true do |t|
    t.string   "name"
    t.string   "animation_label"
    t.decimal  "hp_min",          :precision => 12, :scale => 6
    t.decimal  "hp_max",          :precision => 12, :scale => 6
    t.decimal  "ap_min",          :precision => 12, :scale => 6
    t.decimal  "ap_max",          :precision => 12, :scale => 6
    t.string   "target_type"
    t.string   "powerup_type"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.decimal  "status_chance",   :precision => 12, :scale => 6
  end

  create_table "game_quest_objectives", :force => true do |t|
    t.string   "name"
    t.string   "quest_id"
    t.string   "description"
    t.string   "objective_type"
    t.string   "objective_sub_type"
    t.string   "objective_detail"
    t.string   "associated_id"
    t.integer  "qty"
    t.string   "icon_asset_id"
    t.string   "finish_cost_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "hint_text"
    t.integer  "sequence_number"
    t.integer  "count"
  end

  create_table "game_quests", :force => true do |t|
    t.string   "name"
    t.string   "trigger_type"
    t.string   "trigger_id"
    t.decimal  "probability",        :precision => 12, :scale => 6
    t.string   "reward_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "disp_name"
    t.string   "advisor_id"
    t.string   "hint_text"
    t.string   "icon_asset_id"
    t.boolean  "trigger_on_end"
    t.string   "completion_text"
    t.string   "description"
    t.string   "complete_dialog_id"
    t.string   "trigger_dialog_id"
    t.string   "view_reward_id"
    t.string   "next_quest_id"
    t.boolean  "trigger_disabled"
  end

  add_index "game_quests", ["complete_dialog_id"], :name => "fk_game_quests_game_dialog_groups_complete"
  add_index "game_quests", ["reward_id"], :name => "fk_game_quests_game_rewards"
  add_index "game_quests", ["trigger_dialog_id"], :name => "fk_game_quests_game_dialog_groups_trigger"

  create_table "game_recon_missions", :force => true do |t|
    t.string   "name"
    t.string   "description"
    t.string   "local"
    t.integer  "duration"
    t.string   "image_id"
    t.string   "cost_id"
    t.string   "lock_id"
    t.string   "reward_id"
    t.string   "cost_toast_id"
    t.string   "lock_toast_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "level"
  end

  add_index "game_recon_missions", ["reward_id"], :name => "fk_game_recon_missions_game_rewards"

  create_table "game_research_mods", :force => true do |t|
    t.string   "mod_id"
    t.string   "research_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_research_prerequisites", :force => true do |t|
    t.string   "research_id"
    t.string   "prerequisite_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_researches", :force => true do |t|
    t.string   "name"
    t.string   "description"
    t.integer  "duration"
    t.string   "icon_asset_id"
    t.string   "lock_id"
    t.string   "cost_id"
    t.string   "reward_id"
    t.string   "upgrade_item_id"
    t.string   "category"
    t.string   "line"
    t.integer  "line_priority"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "finish_cost_id"
    t.string   "lock_toast_id"
    t.string   "cost_toast_id"
    t.string   "small_asset_id"
    t.string   "large_asset_id"
    t.string   "disp_name"
    t.text     "unlocked_items"
  end

  add_index "game_researches", ["reward_id"], :name => "fk_game_researches_game_rewards"

  create_table "game_rewards", :force => true do |t|
    t.string   "name"
    t.string   "description"
    t.integer  "energy"
    t.integer  "soft_currency"
    t.integer  "hard_currency"
    t.integer  "xp"
    t.string   "icon_asset_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "max_energy"
    t.integer  "challenge_points"
    t.integer  "max_challenge_points"
    t.string   "item_collection_id"
    t.text     "details"
  end

  add_index "game_rewards", ["item_collection_id"], :name => "fk_game_rewards_game_item_collections"

  create_table "game_rewards_collections", :force => true do |t|
    t.string   "item_collection_id",                 :null => false
    t.string   "reward_id",          :default => "", :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "game_rewards_collections", ["reward_id"], :name => "fk_game_rewards_collections_game_rewards"

  create_table "game_secret_chapters", :force => true do |t|
    t.string   "name"
    t.string   "chapter_id"
    t.integer  "activation_time",   :default => 0, :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "hero_reward_id"
    t.boolean  "is_enabled"
    t.integer  "deactivation_time", :default => 0, :null => false
  end

  add_index "game_secret_chapters", ["chapter_id"], :name => "fk_game_secret_chapters_game_chapters"

  create_table "game_sound_settings", :force => true do |t|
    t.string   "name"
    t.string   "asset_id"
    t.decimal  "pan",         :precision => 12, :scale => 6
    t.integer  "loop"
    t.decimal  "start_delay", :precision => 12, :scale => 6
    t.integer  "sound_type"
    t.decimal  "volume",      :precision => 12, :scale => 6
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_static_settings", :force => true do |t|
    t.string   "name"
    t.string   "value"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "setting_type"
  end

  create_table "game_status_attributes", :force => true do |t|
    t.string   "name"
    t.string   "value"
    t.text     "status_id",  :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_status_children", :force => true do |t|
    t.string   "parent_status_id", :null => false
    t.string   "child_status_id",  :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "game_status_children", ["child_status_id"], :name => "fk_game_status_children_game_statuses_child"
  add_index "game_status_children", ["parent_status_id"], :name => "fk_game_status_children_game_statuses_parent"

  create_table "game_statuses", :force => true do |t|
    t.string   "name"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "class_name"
    t.string   "icon_asset_id"
    t.string   "disp_name"
    t.string   "duration"
    t.boolean  "is_boon"
    t.string   "description"
    t.string   "anim_id"
    t.boolean  "visible"
    t.boolean  "permanent"
    t.string   "duration_on"
    t.string   "triggers_on"
    t.decimal  "trigger_chance",  :precision => 12, :scale => 6
    t.string   "count"
    t.string   "tags"
    t.string   "or_tags"
    t.string   "not_tags"
    t.decimal  "scalar",          :precision => 12, :scale => 6
    t.integer  "flat"
    t.string   "stack_group"
    t.integer  "stack_max"
    t.text     "spawn_status_id"
  end

  create_table "game_store_tags", :force => true do |t|
    t.string   "name"
    t.string   "disp_name"
    t.integer  "sequence_number"
    t.string   "color"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "icon_asset_id"
  end

  create_table "game_uniform_slots", :force => true do |t|
    t.string   "name"
    t.string   "uniform_id"
    t.integer  "sequence_number"
    t.string   "lock_id"
    t.string   "cost_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_uniform_statuses", :force => true do |t|
    t.text     "status_id",  :null => false
    t.text     "uniform_id", :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_uniforms", :force => true do |t|
    t.string   "name"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.boolean  "hero_only"
    t.string   "hero_id"
    t.string   "left_toast_id"
    t.string   "right_toast_id"
  end

  create_table "game_unlocks", :force => true do |t|
    t.string   "name"
    t.string   "unlock_type"
    t.string   "reward_id"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "target_id"
  end

  add_index "game_unlocks", ["reward_id"], :name => "fk_game_unlocks_game_rewards"

  create_table "game_user_templates", :force => true do |t|
    t.string   "name"
    t.string   "template_type"
    t.integer  "soft_currency",       :default => 0
    t.integer  "energy",              :default => 0
    t.integer  "hard_currency",       :default => 0
    t.text     "items"
    t.text     "heroes"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.text     "avatar_slots"
    t.text     "researches"
    t.string   "flight_map"
    t.string   "start_quest"
    t.integer  "challenge_points"
    t.string   "default_strike_team"
    t.string   "missions"
  end

  create_table "game_villain_actions", :force => true do |t|
    t.text     "action_id",  :null => false
    t.integer  "level"
    t.text     "villain_id", :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_villain_rewards", :force => true do |t|
    t.string   "villain_id"
    t.string   "reward_id"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "game_villain_rewards", ["reward_id"], :name => "fk_game_villain_rewards_game_rewards"

  create_table "game_villain_statuses", :force => true do |t|
    t.text     "status_id",  :null => false
    t.text     "villain_id", :null => false
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_villains", :force => true do |t|
    t.string   "name"
    t.string   "swf_asset_id"
    t.string   "icon_asset_id"
    t.string   "controller"
    t.integer  "xp"
    t.integer  "health_rating"
    t.integer  "stamina_rating"
    t.integer  "attack_rating"
    t.integer  "defense_rating"
    t.integer  "accuracy_rating"
    t.integer  "evasion_rating"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "default_counter"
    t.string   "tags"
    t.text     "description"
    t.string   "disp_name"
    t.text     "bio"
    t.integer  "initiative"
    t.string   "rps_type"
    t.string   "loot_table_id"
    t.string   "right_asset_id"
    t.string   "left_asset_id"
    t.string   "cog_name"
    t.string   "cog_bio"
    t.string   "cog_asset_id"
  end

  create_table "game_viral_errors", :force => true do |t|
    t.string   "name"
    t.string   "title"
    t.string   "description"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_viral_events", :force => true do |t|
    t.string   "name"
    t.string   "game_event"
    t.string   "viral_event"
    t.string   "params"
    t.string   "icon_asset_id"
    t.string   "reward_id"
    t.string   "action"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "game_xp_levels", :force => true do |t|
    t.integer  "xp"
    t.integer  "level"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "cost_id"
    t.string   "reward_id"
    t.string   "hero_id"
    t.integer  "training_duration"
    t.decimal  "fd_mission_sp_multiplier", :precision => 12, :scale => 6, :default => 0.0
    t.decimal  "fd_mission_xp_multiplier", :precision => 12, :scale => 6, :default => 0.0
    t.string   "buy_cost_id"
    t.string   "name"
  end

  add_index "game_xp_levels", ["buy_cost_id"], :name => "index_game_xp_levels_on_buy_cost_id", :unique => true
  add_index "game_xp_levels", ["hero_id"], :name => "fk_game_xp_levels_game_heroes"

  create_table "repo_branches", :force => true do |t|
    t.string   "repo"
    t.string   "branch"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "servers", :force => true do |t|
    t.string   "name"
    t.string   "tag_deployed"
    t.boolean  "locked"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "table_histories", :force => true do |t|
    t.string   "table_name"
    t.string   "action"
    t.string   "row_id"
    t.string   "user_updated"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  create_table "table_locks", :force => true do |t|
    t.string   "table_name"
    t.string   "user"
    t.boolean  "locked"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

end
