# frozen_string_literal: true

def decimal_to_binary(number)
  result = ''
  while number != 0 do
    result += (number%2).to_s
    number /= 2
  end
  puts result.reverse
end

decimal_to_binary(52)