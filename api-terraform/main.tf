provider "aws" {
  region = "af-south-1"
}

resource "aws_vpc" "task_time_tracker_vpc" {
  cidr_block = "10.0.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true
  tags = {
    Name = "task-time-tracker-vpc"
  }
}

resource "aws_subnet" "task_time_tracker_subnet" {
  vpc_id                  = aws_vpc.task_time_tracker_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "af-south-1a"
  map_public_ip_on_launch = true
  tags = {
    Name = "task-time-tracker-subnet"
  }
}

resource "aws_internet_gateway" "task_time_tracker_igw" {
  vpc_id = aws_vpc.task_time_tracker_vpc.id
  tags = {
    Name = "task-time-tracker-igw"
  }
}

resource "aws_route_table" "task_time_tracker_route_table" {
  vpc_id = aws_vpc.task_time_tracker_vpc.id
}

resource "aws_route" "task_time_tracker_route" {
  route_table_id         = aws_route_table.task_time_tracker_route_table.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.task_time_tracker_igw.id
}

resource "aws_route_table_association" "task_time_tracker_route_table_association" {
  subnet_id      = aws_subnet.task_time_tracker_subnet.id
  route_table_id = aws_route_table.task_time_tracker_route_table.id
}

resource "aws_security_group" "task_time_tracker_sg" {
  vpc_id      = aws_vpc.task_time_tracker_vpc.id
  description = "Allow inbound SSH and HTTP traffic"
  
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # Open to all IPs (for HTTP access)
  }

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]  # Open port 8080 for the Spring Boot app
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"  
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "task-time-tracker-sg"
  }
}

resource "aws_instance" "task_time_tracker_instance" {
  ami           = "ami-00d6d5db7a745ff3f"  
  instance_type = "t3.micro"               
  key_name      = "task_time_tracker_key"  

  subnet_id     = aws_subnet.task_time_tracker_subnet.id
  security_groups = [aws_security_group.task_time_tracker_sg.id]

  associate_public_ip_address = true

  tags = {
    Name = "TaskTimeTrackerApplication"
  }
}

output "instance_public_ip" {
  value = aws_instance.task_time_tracker_instance.public_ip
}

