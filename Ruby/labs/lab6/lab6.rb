class Student
  attr_accessor :last_name, :need_dormitory, :length_of_service, :worked_as_teacher, :school_type, :language_studied

  def initialize(last_name, need_dormitory, length_of_service, worked_as_teacher, school_type, language_studied)
    @last_name = last_name
    @need_dormitory = need_dormitory
    @length_of_service = length_of_service
    @worked_as_teacher = worked_as_teacher
    @school_type = school_type
    @language_studied = language_studied
  end
end

class University
  attr_accessor :students

  def initialize
    @students = []
  end

  def add_student(student)
    @students << student
  end

  def students_needing_dormitory
    @students.select { |student| student.need_dormitory }
  end

  def teachers_with_experience(years)
    @students.select { |student| student.worked_as_teacher && student.length_of_service >= years }
  end

  def pedagogical_school_graduates
    @students.select { |student| student.school_type == 'pedagogical' }
  end

  def language_groups
    language_groups = Hash.new { |hash, key| hash[key] = [] }

    @students.each do |student|
      language_groups[student.language_studied] << student
    end

    language_groups
  end
end

# Example usage:

university = University.new

# Adding students to the university
university.add_student(Student.new('Doe', true, 3, true, 'pedagogical', 'English'))
university.add_student(Student.new('Smith', false, 1, false, 'engineering', 'French'))
university.add_student(Student.new('Johnson', true, 2, true, 'pedagogical', 'Spanish'))
university.add_student(Student.new('Brown', false, 4, false, 'engineering', 'English'))
university.add_student(Student.new('Davis', true, 5, true, 'pedagogical', 'French'))

puts "Students needing dormitory:"
puts university.students_needing_dormitory.map(&:last_name)

puts "\nTeachers with 2 or more years of experience:"
puts university.teachers_with_experience(2).map(&:last_name)

puts "\nPedagogical school graduates:"
puts university.pedagogical_school_graduates.map(&:last_name)

puts "\nLanguage groups:"
university.language_groups.each do |language, students|
  puts "#{language.capitalize}: #{students.map(&:last_name).join(', ')}"
end
