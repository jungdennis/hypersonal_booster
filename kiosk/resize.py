from PIL import Image
image = Image.open('images/protein.png')
resize_image = image.resize((300,400))
resize_image.save('images/protein_resize.png')
