require 'benchmark'
require 'time'



def parse(common)
  default_user_data = {:paths => {}, :segments => {}}
  [:a, :b, :c].each do |n|
    default_user_data[:segments][n] = {:value => nil, :time => nil}
  end
  File.open('data.txt') do |file|
    file.each_line do |line|
      data = line.chomp.split("\t")
      time = Time.parse(data[0]).to_i
      #time = data[0].to_i
      user = data[1]
      url = data[2]
      common[user] ||= default_user_data.dup
      segments = common[user][:segments]
  
      unless segments[:a][:value]
        segments[:a][:value] = url
        segments[:a][:time] = time
        next
      end 
      unless segments[:b][:value]
        segments[:b][:value] = url
        segments[:b][:time] = time
        next
      end
    
      if (time - segments[:b][:time]) > 1200
        segments[:a] = {:time => time, :value => url}
        segments[:b] = {:time => nil, :value => nil}
        next
      elsif (time - segments[:a][:time]) > 1200
        segments[:a] = segments[:b]
        segments[:b] = {:time => time, :value => url}
        next
      end
    
    
      segments[:c] = {:time => time, :value => url}
    
      key = "#{segments[:a][:value]}#{segments[:b][:value]}#{segments[:c][:value]}"
      common[:paths][key] ||= 0
      common[:paths][key] += 1
      segments[:a] = segments[:b]
      segments[:b] = segments[:c]
    end
  end
  paths = common[:paths].sort {|a,b| a[1] <=> b[1]}
  puts paths.inspect
end



def create(urls)
  set = []
  start_time = Time.now - 86400
  File.open('data.txt','w') do |file|
    x = 0
    2000000.times do |i|
      r = urls[rand(urls.size - 1)]
      if x > 5
        start_time = start_time + 1
        x = 0
      end
      user = rand(10000)
      file.write("#{start_time}\t#{user}\t#{r}\n")
      #set << [start_time.dup,user,r]
      x += 1
    end
  end
end


urls = [:a, :b, :c, :d, :e, :f, :g, :h, :i, :j, :k, :l, :m, :n, :o, :p]

common = {:paths => {}}

#create(urls)
puts "create done"
puts Benchmark.realtime {parse(common)}

#puts common.inspect

#sm01 g63 - g85