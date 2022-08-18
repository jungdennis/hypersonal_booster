from PIL import Image
image = Image.open('images/down.png')
resize_image = image.resize((70,70))
resize_image.save('images/down_resize.png')
