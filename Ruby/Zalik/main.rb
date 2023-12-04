require 'date'

class Event
  attr_accessor :date, :importance

  def initialize(date, importance)
    @date = date
    @importance = importance
  end
end

class SingleEvent < Event
end

class RecurringEvent < Event
  attr_accessor :frequency

  def initialize(date, importance, frequency)
    super(date, importance)
    @frequency = frequency
  end

  def final_occurrence(end_date)
    current_occurrence = @date

    case @frequency
    when 'daily'
      current_occurrence += 1 until current_occurrence > end_date
    when 'weekly'
      current_occurrence += 7 until current_occurrence > end_date
    when 'monthly'
      current_occurrence = current_occurrence.next_month until current_occurrence > end_date
    else
      # Handle other frequencies as needed
      current_occurrence
    end

    current_occurrence
  end
end


class Organizer
  attr_accessor :events

  def initialize
    @events = []
  end

  def add_event(event)
    @events << event
  end

  def find_events_by_date(start_date, end_date)
    @events.select { |event| event.date.between?(start_date, end_date) }
  end

  def find_events_by_importance(importance)
    @events.select { |event| event.importance == importance }
  end

  def sort_events_by_date
    @events.sort_by(&:date)
  end

  def sort_events_by_importance
    @events.sort_by(&:importance).reverse
  end

  def upcoming_events(num)
    @events.sort_by(&:date).first(num)
  end

  def remind_upcoming_events(today, period)
    end_date = today + period
    upcoming = []

    @events.each do |event|
      if event.is_a?(RecurringEvent)
        final_date = event.final_occurrence(end_date)
        upcoming << event if final_date >= today
      else
        upcoming << event if event.date.between?(today, end_date)
      end
    end

    puts "\nПодії в періоді з #{today} до #{end_date}:"
    upcoming.each do |event|
      puts "Дата: #{event.date}, Важливість: #{event.importance}"
    end
  end
end


organizer = Organizer.new

event1 = SingleEvent.new(Date.new(2023, 12, 10), 3)
event2 = RecurringEvent.new(Date.new(2023, 11, 15), 5, 'monthly')
event3 = SingleEvent.new(Date.new(2023, 12, 5), 2)

organizer.add_event(event1)
organizer.add_event(event2)
organizer.add_event(event3)

sorted_by_date = organizer.sort_events_by_date
sorted_by_importance = organizer.sort_events_by_importance
result_events = organizer.find_events_by_date(Date.new(2023, 12, 1), Date.new(2023, 12, 15))

# Вивід результатів
puts "Події в заданому діапазоні дат:"
result_events.each do |event|
  puts "Дата: #{event.date}, Важливість: #{event.importance}"
end

puts "\nПодії, відсортовані за датою:"
sorted_by_date.each do |event|
  puts "Дата: #{event.date}, Важливість: #{event.importance}"
end

puts "\nПодії, відсортовані за важливістю:"
sorted_by_importance.each do |event|
  puts "Дата: #{event.date}, Важливість: #{event.importance}"
end

organizer.remind_upcoming_events(Date.new(2024, 12, 8), 7)