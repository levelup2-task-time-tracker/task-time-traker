# VPC
resource "aws_vpc" "my_vpc" {
  cidr_block           = var.vpc_cidr_block
  enable_dns_hostnames = true
}

# Internet gateway
resource "aws_internet_gateway" "task_time_tracker_db_gateway" {
  vpc_id = aws_vpc.my_vpc.id
}

# Public subnets
resource "aws_subnet" "subnet_a" {
  vpc_id            = aws_vpc.my_vpc.id
  cidr_block        = cidrsubnet(var.vpc_cidr_block, 8, 1)
  availability_zone = "af-south-1a"
}

resource "aws_subnet" "subnet_b" {
  vpc_id            = aws_vpc.my_vpc.id
  cidr_block        = cidrsubnet(var.vpc_cidr_block, 8, 2)
  availability_zone = "af-south-1b"
}

# Routing
resource "aws_route_table" "routedb" {
  vpc_id = aws_vpc.my_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.task_time_tracker_db_gateway.id
  }
}

# Public Subnets group
resource "aws_db_subnet_group" "task_time_tracker_db_subnet_group" {
  name       = "aws_subnet_group_task_time_tracker_db"
  subnet_ids = [aws_subnet.subnet_a.id, aws_subnet.subnet_b.id]
}

resource "aws_route_table_association" "subnet_a_association" {
  subnet_id      = aws_subnet.subnet_a.id
  route_table_id = aws_route_table.routedb.id
}

resource "aws_route_table_association" "subnet_b_association" {
  subnet_id      = aws_subnet.subnet_b.id
  route_table_id = aws_route_table.routedb.id
}

# Security Group
resource "aws_security_group" "allow_tls" {
  name        = "allow_tls"
  description = "Allow TLS inbound traffic and all outbound traffic"
  vpc_id      = aws_vpc.my_vpc.id
}

resource "aws_vpc_security_group_ingress_rule" "allow_tcp" {
  security_group_id = aws_security_group.allow_tls.id
  from_port         = var.db_port
  to_port           = var.db_port
  ip_protocol       = "tcp"
  cidr_ipv4         = "0.0.0.0/0"
}

resource "aws_vpc_security_group_egress_rule" "allow_tcp" {
  security_group_id = aws_security_group.allow_tls.id
  from_port         = var.db_port
  to_port           = var.db_port
  ip_protocol       = "tcp"
  cidr_ipv4         = "0.0.0.0/0"
}

# RDS instance
resource "aws_db_instance" "task_time_tracker_instance" {
  identifier          = "task-time-tracker-db"
  engine              = "postgres"
  engine_version      = "17.4"
  instance_class      = "db.t4g.micro"
  allocated_storage   = 20
  publicly_accessible = true
  backup_retention_period = 7
  backup_window           = "03:00-04:00"
  maintenance_window      = "mon:05:00-mon:06:00"
  skip_final_snapshot     = true
  
  db_subnet_group_name   = aws_db_subnet_group.task_time_tracker_db_subnet_group.name
  vpc_security_group_ids = [aws_security_group.allow_tls.id]
  port                   = var.db_port
  username               = var.db_username
  password               = var.db_password
}
