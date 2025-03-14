

provider "aws" {
  region = "af-south-1"
}

resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name = "main_vpc"
  }
}

resource "aws_subnet" "main" {
  count = 2
  vpc_id            = aws_vpc.main.id
  cidr_block        = cidrsubnet(aws_vpc.main.cidr_block, 8, count.index)
  availability_zone = element(["af-south-1a", "af-south-1b"], count.index)

  tags = {
    Name = "main_subnet_${count.index}"
  }
}


resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "main_igw"
  }
}


resource "aws_route_table" "main" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }

  tags = {
    Name = "main_route_table"
  }
}


resource "aws_route_table_association" "main" {
  count          = 2
  subnet_id      = aws_subnet.main[count.index].id
  route_table_id = aws_route_table.main.id
}

resource "aws_db_instance" "task-time-tracker-db" {
  identifier             = "task-time-tracker-db"
  instance_class         = "db.t4g.micro"
  allocated_storage      = 20
  storage_type           = "gp2"
  engine                 = "postgres"
  engine_version         = "16.3"
  username               = var.DB_USERNAME
  password               = var.DB_PASSWORD
  publicly_accessible    = true
  skip_final_snapshot    = true
  vpc_security_group_ids = [aws_security_group.task_time_tracker_db.id]
  db_subnet_group_name   = aws_db_subnet_group.task_time_tracker_db.name

  tags = {
    name = "task-time-tracker-db"
  }
}

resource "aws_security_group" "task_time_tracker_db" {
  vpc_id      = aws_vpc.main.id
  name        = "rds_sg"
  description = "Allow inbound traffic to RDS"
  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_subnet_group" "task_time_tracker_db" {
  name       = "task_time_tracker_db_rds_subnet_group"
  subnet_ids = aws_subnet.main[*].id
  tags = {
    Name = "task_time_tracker_db_rds_subnet_group"
  }
}