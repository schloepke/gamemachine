module GameMachine
  module DataStores
    class Jdbc

      attr_reader :serialization
      def initialize(serialization)
        @serialization = serialization
      end
      
      def dbname
        'game_machine'
      end

      def dbtype
        @dbtype ||= case Application.config.jdbc.driver
          when 'org.postgresql.Driver'
            :postgres
          when 'com.mysql.jdbc.Driver'
            :mysql
          else
            raise "Unknown JDBC driver #{Application.config.jdbc.driver}"
          end
      end
        
      def connect
        @pool ||= GameMachine::JavaLib::DbConnectionPool.getInstance
        unless @pool.connect(
          dbname,
          Application.config.jdbc.hostname,
          Application.config.jdbc.port,
          Application.config.jdbc.database,
          Application.config.jdbc.ds,
          Application.config.jdbc.username,
          Application.config.jdbc.password || ''
        )
          GameMachine.logger.error "Unable to establish database connection, exiting"
          System.exit 1
        end
        @pool
      end

      # type 0 = string, 1 = byte array
      def set(id,value)
        connection = @pool.get_connection(dbname)
        if value.is_a?(String)
          value = value.to_java_bytes
          type = 0
        else
          type = 1
        end

        if dbtype == :mysql
          s = connection.prepare_statement("INSERT INTO entities (id,value,datatype) VALUES (?,?,?) ON DUPLICATE KEY UPDATE value=VALUES(value)")
        else
          s = connection.prepare_statement("INSERT INTO entities (id,value,datatype) VALUES (?,?,?)")
        end
        
        s.setString(1,id.to_java_string)
        s.setBytes(2,value)
        s.setInt(3,type)
        s.execute_update
        s.close
        connection.close
      end

      def delete(id)
        connection = @pool.get_connection(dbname)
        s = connection.prepare_statement("DELETE from entities where id = ?")
        s.setString(1,id.to_java_string)
        s.execute_update
        s.close
        connection.close
      end

      def get(key)
        value = nil
        type = nil
        connection = @pool.get_connection(dbname)
        s = connection.create_statement
        res = s.execute_query("SELECT value,datatype from entities where id = '#{key}' LIMIT 1")
        if res.next
          value = res.get_bytes('value')
          type = res.get_int("datatype")
        end

        s.close
        connection.close

        if type == 0
          value.to_s
        else
          value
        end
      end

    end
  end
end
