-- molecules
INSERT INTO definition (trig, primerep, pos, type) VALUES ('#animal', 'NOT,PEOPLE', null, 'molecule');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('#action', 'DO,SOMETHING', null, 'molecule');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('#mouth','BODY,PART,SMALL', null, 'molecule');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('#eat', '#action,#mouth', null, 'molecule');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('#food','SOMETHING,#eat', null,'molecule');

-- words
INSERT INTO definition (trig, primerep, pos, type) VALUES ('the', 'THE', 'Det', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('cat', 'SMALL,#animal', 'N', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('slept', 'NOT,#action,LONG TIME,BEFORE', 'V', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('on', 'ABOVE,TOUCH', 'P', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('floor', 'BELOW,ALL,THING', 'N', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('tiger', 'BIG,#animal,BAD', 'N', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('in', 'INSIDE', 'P', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('park', 'BIG,PLACE', 'N', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('did','DO,BEFORE','V','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('beast','VERY,BIG,#animal,BAD','N','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('sleep','NOT,#action,LONG TIME','V','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('loiter','NOT,#action','V','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('yesterday','BEFORE','N','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('joseph','PEOPLE','Det','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('ate','#eat,BEFORE','V','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('a','ONE','Det','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('burger','#food','N','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('eat','#eat','V','word');

INSERT INTO definition (trig, primerep, pos, type) VALUES ('dodo', '#animal', 'N', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('apple', '#food', 'N', 'word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('an','ONE','Det','word');

-- question words
INSERT INTO definition (trig, primerep, pos, type) VALUES ('where','&Space','Q','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('what','&All','Q','word');
INSERT INTO definition (trig, primerep, pos, type) VALUES ('when','&Time','Q','word');

--INSERT INTO definitions (trig, primerep, pos, type) VALUES ('','','','');