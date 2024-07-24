import csv

data = [
    {"name": f"User{i}", "age": 20 + i % 30}
    for i in range(200)
]

file_path = "./users.csv"

with open(file_path, mode='w', newline='') as file:
    writer = csv.DictWriter(file, fieldnames=["name", "age"])
    writer.writeheader()
    writer.writerows(data)

file_path
