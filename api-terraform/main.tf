provider "aws" {
  region = "af-south-1"
}

resource "aws_security_group" "task_time_tracker_sg" {
  vpc_id      = "vpc-0d4692da6f5a22b8a"
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

