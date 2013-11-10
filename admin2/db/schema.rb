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
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20131110041550) do

  create_table "app_servers", force: true do |t|
    t.string   "name"
    t.integer  "user_id"
    t.integer  "cluster_id"
    t.string   "environment"
    t.string   "game_handler"
    t.string   "data_store"
    t.integer  "cache_write_interval",              default: -1
    t.integer  "cache_writes_per_second",           default: -1
    t.integer  "world_grid_size",                   default: 2000
    t.integer  "world_grid_cell_size",              default: 50
    t.boolean  "mono_enabled",                      default: false
    t.integer  "singleton_manager_router_count",    default: 10
    t.integer  "singleton_manager_update_interval", default: 100
    t.string   "couchbase_servers"
    t.string   "auth_handler"
    t.boolean  "http_enabled",                      default: false
    t.string   "http_host"
    t.integer  "http_port"
    t.boolean  "udp_enabled",                       default: false
    t.string   "udp_host"
    t.integer  "udp_port"
    t.boolean  "udt_enabled",                       default: false
    t.string   "udt_host"
    t.integer  "udt_port"
    t.boolean  "tcp_enabled",                       default: false
    t.string   "tcp_host"
    t.integer  "tcp_port"
    t.string   "akka_host"
    t.integer  "akka_port"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "app_servers", ["name", "user_id"], name: "index_app_servers_on_name_and_user_id", unique: true, using: :btree

  create_table "clusters", force: true do |t|
    t.string  "name"
    t.string  "seeds"
    t.integer "user_id"
  end

  create_table "users", force: true do |t|
    t.string   "email",                  default: "", null: false
    t.string   "encrypted_password",     default: "", null: false
    t.string   "reset_password_token"
    t.datetime "reset_password_sent_at"
    t.datetime "remember_created_at"
    t.integer  "sign_in_count",          default: 0,  null: false
    t.datetime "current_sign_in_at"
    t.datetime "last_sign_in_at"
    t.string   "current_sign_in_ip"
    t.string   "last_sign_in_ip"
    t.datetime "created_at"
    t.datetime "updated_at"
  end

  add_index "users", ["email"], name: "index_users_on_email", unique: true, using: :btree
  add_index "users", ["reset_password_token"], name: "index_users_on_reset_password_token", unique: true, using: :btree

end
