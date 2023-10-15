# frozen_string_literal: true

A = true
B = true
C = false

X = 12
Y = 3
Z = -2

# task a)
task_a = !(A || B) && (A && !B)
puts task_a


# task b)
task_b = (Z != Y) || (6 >= Y) && A || B && C && X >= 1.5
puts task_b


# task c)
task_c = (8 - X * 2 <= Z) && (X**2 >= Y**2) || (Z ≥ 15)
puts task_c


# task d)
task_d = X > 0 && Y < 0 || Z >= (X * Y - Y / X) - (-Z)
puts task_d


# task e)
task_e = !(A || B && !(C || (!A || B)))
puts task_e


# task f)
task_f = X**2 + Y**2 >= 1 && X >= 0 && Y >= 0
puts task_f

# task g)
task_g = (A && (C && B <=> B || A) || C) && B
puts task_g



##############

x = 4
P = false

second = (Math.log(x, Math::E) < x) && !P && (Math.sqrt(x) > x*x) || (2*x==x)
puts second