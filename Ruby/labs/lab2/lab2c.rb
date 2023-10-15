# frozen_string_literal: true

def binary_to_decimal(number)
  result = 0
  for i in 0...number.length
    result += number[i].to_i * 2 ** (number.length - i - 1)
  end
  puts result
end

binary_to_decimal('1110')