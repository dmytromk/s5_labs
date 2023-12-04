class Note
  attr_accessor :title, :content, :created_at, :updated_at, :tags, :importance

  def initialize(title, content, tags = [], importance = 0)
    @title = title
    @content = content
    @created_at = Time.now
    @updated_at = @created_at + rand(100)
    @tags = tags
    @importance = importance
  end
end

module Sortable
  def sort_by_creation_time(notes)
    notes.sort_by(&:created_at)
  end

  def sort_by_last_update_time(notes)
    notes.sort_by(&:updated_at)
  end

  def sort_by_importance(notes)
    notes.sort_by(&:importance).reverse
  end
end

module Searchable
  def search_by_content(notes, keyword)
    notes.select { |note| note.content.include?(keyword) }
  end

  def search_by_tags(notes, tags)
    notes.select { |note| (tags - note.tags).empty? }
  end

  def search_by_time_range(notes, start_time, end_time)
    notes.select { |note| note.created_at.between?(start_time, end_time) }
  end
end

class ImageNote < Note
  attr_accessor :image_path

  def initialize(title, content, image_path, tags = [], importance = 0)
    super(title, content, tags, importance)
    @image_path = image_path
  end
end

class TextNote < Note
end

class ChecklistNote < Note
  attr_accessor :checklist_items

  def initialize(title, checklist_items, tags = [], importance = 0)
    super(title, "", tags, importance)
    @checklist_items = checklist_items
  end
end

class NoteManager
  include Sortable
  include Searchable

  attr_accessor :notes

  def initialize(notes = [])
    @notes = notes
  end

  def display_notes(notes)
    notes.each do |note|
      puts "Title: #{note.title}"
      puts "Content: #{note.content}"
      puts "Tags: #{note.tags.join(', ')}"
      puts "Importance: #{note.importance}"
      puts "Created At: #{note.created_at}"
      puts "Updated At: #{note.updated_at}"
      puts "\n"
    end
  end
end

# Додавання декількох нотаток до менеджера
image_note = ImageNote.new("Nature", "Beautiful landscape", "/path/to/image.jpg", ["nature", "landscape"], 5)
text_note = TextNote.new("Meeting", "Discuss project plan", ["meeting"], 3)
checklist_note = ChecklistNote.new("Shopping", ["Milk", "Eggs", "Bread"], ["shopping"], 2)

note_manager = NoteManager.new([image_note, text_note, checklist_note])

# Сортування по часу
sorted_notes_by_update_time = note_manager.sort_by_last_update_time(note_manager.notes)
puts "\nSorted Notes by Last Update Time:"
note_manager.display_notes(sorted_notes_by_update_time)

# Пошук
searched_notes_by_tags = note_manager.search_by_tags(note_manager.notes, ["nature"])
puts "\nSearched Notes by Tags:"
note_manager.display_notes(searched_notes_by_tags)

# Додавання нової нотатки
new_text_note = TextNote.new("\nNew Note", "This is a new note", ["new"], 4)
note_manager.notes << new_text_note

# Сортування по важливості
sorted_notes_by_importance = note_manager.sort_by_importance(note_manager.notes)
puts "\nSorted Notes by Importance:"
note_manager.display_notes(sorted_notes_by_importance)
