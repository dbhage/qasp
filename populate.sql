-- molecules
INSERT INTO definition (trig, primerep, pos, type) VALUES ('#animal', 'NOT(PEOPLE)', null, 'molecule');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('#action', 'DO(SOMETHING)', null, 'molecule');

-- words
INSERT INTO definition (trig, primerep, pos, type) VALUES ('the', 'THE(?X)', 'Det', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('cat', 'SMALL,#animal', 'N', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('slept', 'NOT(DO(SOMETHING)),LONG TIME,BEFORE)', 'V', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('on', 'ABOVE(TOUCH)', 'P', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('floor', 'BELOW(ALL(THING))', 'N', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('tiger', 'BIG,#animal,BAD', 'N', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('in', 'INSIDE(?x)', 'P', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('park', 'BIG,PLACE', 'N', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('did','DO,BEFORE','V','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('beast','VERY(BIG),#animal,BAD','N','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('sleep','NOT(#action),LONG TIME','V','word');
INSERT INTO definitions (trig, primerep, pos, type) VALUES ('where','?x(*,&Space,*)','Q','word');
INSERT INTO definitions (trig, primerep, pos, type) VALUES ('loiter','NOT(#action)','V','word');
INSERT INTO definitions (trig, primerep, pos, type) VALUES ('yesterday','BEFORE','N','word');
--INSERT INTO definitions (trig, primerep, pos, type) VALUES ('','','','');