-- session table
create table session (
    sessionid varchar(10) not null, 
    timeStarted bigint not null, 
    timeEnded bigint, 
    primary key (sessionid)
) engine=InnoDB;

-- definition table
create table definition (
    defid int not null auto_increment, 
    trig text not null, 
    primerep text not null, 
    pos enum('N', 'V', 'P', 'Det', 'NP', 'PP', 'S', 'O', 'Q'), 
    primary key (defid)
) engine=InnoDB;

-- primes table
create table primes (
    text varchar(12) not null, 
    category varchar(41) not null, 
    valency enum('1', '2', '3') not null, 
    primary key (text)
) engine=InnoDB;

-- state table
create table state (
    stateid int not null, 
    characteristic varchar(20) not null, 
    sessionid char(10) not null, 
    primary key (stateid, sessionid), 
    foreign key (sessionid) references session(sessionid) on delete cascade on update cascade
) engine=InnoDB;

-- event table
create table event (
    eventid int not null, 
    verb varchar(20), 
    object varchar(100), 
    sessionid char(10) not null, 
    primary key (eventid, sessionid), 
    foreign key (sessionid) references session(sessionid) on delete cascade on update cascade
) engine=InnoDB;

-- words table
create table words (
    defid int not null, 
    sessionid char(10) not null, 
    primary key (defid, sessionid), 
    foreign key (defid) references definition(defid) on delete no action on update no action, 
    foreign key (sessionid) references session(sessionid) on update cascade on delete cascade
) engine=InnoDB;

-- text table
create table text (
    trigger text not null, 
    sessionid char(10) not null, 
    primary key (trigger, sessionid), 
    foreign key (trigger) references definition(trigger) on delete no action on update no action, 
    foreign key (sessionid) references session(sessionid) on update cascade on delete cascade
) engine=InnoDB;

create table concept (
    conceptid int not null, 
    text text not null, 
    type enum('STATE', 'EVENT') not null, 
    sessionid char(10) not null, 
    primary key (conceptid, sessionid), 
    foreign key (sessionid) references session(sessionid) on update cascade on delete cascade
) engine=InnoDB;

create table sessionmoleculesconcept (
    trigger text not null, 
    sessionid char(10) not null, 
    conceptid int not null, 
    primary key (trigger, conceptid, sessionid), 
    foreign key (trigger) references definition(trigger) on update no action on delete no action, 
    foreign key (sessionid) references session(sessionid) on update cascade on delete cascade, 
    foreign key (conceptid) references concept(conceptid) on update cascade on delete cascade
) engine=InnoDB;

-- conversation table
create table conversation (
    convoid int not null, 
    timeStarted bigint not null, 
    timeEnded bigint, 
    transcript text not null, 
    live enum('0', '1') not null,
    sessionid char(10) not null, 
    primary key(convoid, sessionid), 
    foreign key (sessionid) references session(sessionid) on update cascade on delete cascade
) engine=InnoDB;

-- conversation-concept relationship table
create table conversationconceptrelationship (
    sessionid char(10) not null, 
    convoid int not null, 
    conceptid int not null, 
    primary key (convoid, conceptid, sessionid), 
    foreign key (convoid) references conversation(convoid) on update cascade on delete cascade, 
    foreign key (sessionid) references session(sessionid) on update cascade on delete cascade, 
    foreign key (conceptid) references concept(conceptid) on update cascade on delete cascade) 
engine=InnoDB;
