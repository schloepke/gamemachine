require 'wavefront'
module Wavefront
  class Object
    def triangle_from_face_components face_components
      triangle_vertices = []
      face_components.each do |vertex_str|
        vertex_str_components = vertex_str.split('/').map { |index| index.size > 0 ? index.to_i : nil }
        position_index = vertex_str_components[0]
        tex_index = vertex_str_components[1]

        normal_index = vertex_str_components[2]

        position = vertices[position_index-1]
        tex_coordinate = tex_index ? texture_coordinates[tex_index-1] : nil
        normal = normal_index ? normals[normal_index-1] : nil

        triangle_vertices << Wavefront::Vertex.new(position, tex_coordinate, normal, position_index, tex_index, normal_index)
      end
      Wavefront::Triangle.new triangle_vertices
    end
  end

  class Serializer

    def self.to_protobuf(wavefront_object)
      proto_mesh = GameMachine::MessageLib::Mesh.new
      wavefront_object.groups.each do |group|
        group.triangles.each do |triangle|
          proto_poly = GameMachine::MessageLib::Polygon.new
          if triangle.vertices.size != 3
            puts "Invalid vert count #{triangle.vertices.size}"
          end
          triangle.vertices.each do |vertice|
            proto_vector3 = GameMachine::MessageLib::Vector3.new
            proto_vector3.set_x(vertice.position.x)
            proto_vector3.set_y(vertice.position.y)
            proto_vector3.set_z(vertice.position.z)
            proto_poly.add_vertex(proto_vector3)
          end
          proto_mesh.add_polygon(proto_poly)
        end
      end
      proto_mesh
    end

  end
end