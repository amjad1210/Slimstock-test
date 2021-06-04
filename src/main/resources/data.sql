INSERT INTO VETS (name) VALUES
  ('John'),
  ('Sarah');

INSERT INTO ANIMAL_TYPES (name) VALUES
  ('Dog'),
  ('Cat');

INSERT INTO PETS (name, vet_id, animal_type_id) VALUES
  ('Muffin', 1, 1),
  ('Apple', 2, 2);

INSERT INTO APPOINTMENTS (start, end, pet_id) VALUES
  ('2021-06-18 10:30:00', '2021-06-18 11:30:00', 1),
  ('2021-06-20 11:00:00', '2021-06-20 12:00:00', 2);