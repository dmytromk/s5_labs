# frozen_string_literal: true

def y(x)
  if x > -4 && x <= 0
    ((x-2).abs/(x**2 * Math::cos(x)))**(1.0/7)
  elsif x > 0 && x <= 12
    return 1/((Math.tan(x+1.0/(Math::E**x)))/(Math::sin(x)**2) ** (7.0/2))
  else
    1 / (1.0 + (x / (1.0 + (x / (1.0+Float(x))))))
  end
end

puts y(13)