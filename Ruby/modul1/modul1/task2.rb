# frozen_string_literal: true

class Product
  @@count = 0
  @tune = nil

  attr_reader :id, :name, :shell_life, :price, :producer, :upc, :quantity

  def initialize(*params)
    case params.size
    when 7
      full_constructor(*params)
    when 6
      constructor_without_id(*params)
    when 3
      name_constructor(*params)
    else return
    end

    @@count += 1
  end

  def full_constructor(id, name, producer, price, shell_life, upc, quantity)
    @id = id
    @name = name
    @producer = producer
    @price = price
    @shell_life = shell_life
    @upc = upc
    @quantity = quantity

    @@count += 1
  end

  def constructor_without_id(name, producer, price, shell_life, upc, quantity)
    @id = @@count
    @name = name
    @producer = producer
    @price = price
    @shell_life = shell_life
    @upc = upc
    @quantity = quantity
  end

  def name_constructor(name, producer, shell_life)
    @id = @@count
    @name = name
    @producer = producer
    @shell_life = shell_life
    @price = -1
    @upc = "unknown"
    @quantity = -1
  end

  def getTun
    @tune
  end

  def setTun(t)
    @tune = t
  end

  def to_s
    if @quantity != -1
      "Product: {name: #{@name}; producer: #{@producer}; @price: #{@price}; upc: #{@upc}; shell_life: #{@shell_life}; quantity: #{@quantity}; tun: #{@tune}}"
    else
      "Product: {name: #{@name}; producer: #{@producer}; shell_life: #{@shell_life}; tun: #{@tune}}"
    end
  end

  def self.random_array(size)
    arr = []

    (0..size-1).each do |i|
      arr.append Product.new("name#{i}", "producer#{i}", i ** i)
    end

    [*arr]
  end
end

arr = Product.random_array(5)
arr.push Product.new("product5", "producer1995", 200, 12, "fkdflsdlflfsd", 200)
arr.push Product.new("product6", "producer1994", 150, 11, "asdkkjafkf", 200)
arr.push Product.new("product7", "producer1996", 200, 13, "qwekwetkmgds", 20)
arr.push Product.new("product8", "producer1995", 240, 10, "vdlvcxlkz", 790)

puts "product6:\n #{arr.filter{|el| el.name == "product6"}.map{ |el| el.to_s }} \n\n"
puts "product6 price < 200:\n #{arr.filter{|el| el.price < 200 && el.name == "product6"}.map{ |el| el.to_s }} \n\n"
puts "Shell life > 12:\n #{arr.filter{|el| el.shell_life > 12 }.map{ |el| el.to_s }} \n\n"
