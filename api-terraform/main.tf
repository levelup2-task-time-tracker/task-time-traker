provider "aws" {
  region = "af-south-1"
}

resource "aws_vpc" "task_time_tracker_vpc" {
  cidr_block = "10.0.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true

  tags = {
    Name = "main_vpc"
  }
}

resource "aws_subnet" "task_time_tracker_subnet" {
  count = 2
  vpc_id            = aws_vpc.task_time_tracker_vpc.id
  cidr_block        = cidrsubnet(aws_vpc.task_time_tracker_vpc.cidr_block, 8, count.index)
  availability_zone = element(["af-south-1a", "af-south-1b"], count.index)

  tags = {
    Name = "main_subnet_${count.index}"
  }
}

resource "aws_internet_gateway" "task_time_tracker_igw" {
  vpc_id = aws_vpc.task_time_tracker_vpc.id

  tags = {
    Name = "main_igw"
  }
}

resource "aws_security_group" "task_time_tracker_sg" {
  vpc_id      = aws_vpc.task_time_tracker_vpc.id
  name        = "task-time-tracker-sg"
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
}

resource "aws_instance" "task_time_tracker_instance" {
  ami           = "ami-00d6d5db7a745ff3f"  
  instance_type = "t3.micro"               
  key_name      = "task_time_tracker_key_pair"  

  security_groups = [aws_security_group.task_time_tracker_sg.name]

  associate_public_ip_address = true

  tags = {
    Name = "TaskTimeTrackerApplication"
  }
}

output "instance_public_ip" {
  value = aws_instance.task_time_tracker_instance.public_ip
}

