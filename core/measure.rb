require 'rubygems'
require 'benchmark'

def time_in_seconds_to_interval(seconds,increments_in_minutes=5)
  time = Time.at(seconds)
  Time.at(time.to_f - time.sec - (time.min % increments_in_minutes * 60))
end

def increment_interval(time,increments_in_minutes=5)
  time + (increments_in_minutes * 60)
end

def timerange_for(seconds=Time.now.to_i,increments_in_minutes=5)
  start_time = time_in_seconds_to_interval(seconds)
  end_time = increment_interval(start_time)
  return [start_time,end_time]
end

stats = {}
puts Benchmark.realtime {10000.times {timerange_for.to_s}}

(Time.now.to_i - (86400 * 365)).upto(Time.now.to_i) do |seconds|
  timerange = timerange_for(seconds)
  key = timerange.to_s
  if stats[key]
  else
  stats[key] = {:timerange => timerange, :measurements => {}}
  end
end
puts stats.size
#puts Benchmark.realtime {10000.times {timerange_for}}