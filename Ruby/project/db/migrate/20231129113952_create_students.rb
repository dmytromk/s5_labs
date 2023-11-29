class CreateStudents < ActiveRecord::Migration[7.1]
  def change
    create_table :students do |t|
      t.string :last_name
      t.boolean :need_dormitory
      t.integer :length_of_service
      t.boolean :worked_as_teacher
      t.string :school_type
      t.string :language_studied

      t.timestamps
    end
  end
end
