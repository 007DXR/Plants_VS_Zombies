package core;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.*;
public class pathTest{
    // public static void main(String[] args) {
    //     // E:\大学课件\2022春\Java\程序\Plants_VS_Zombies\plant_vs_zombie_simple\resources/data/entity
    //     // Path dir = Paths.get("E:/大学课件/2022春/Java/程序/PythonPlantsVsZombies-master/resources/graphics/Zombies/BucketheadZombie/BucketheadZombie");
    //     Path dir = Paths.get("resources/data/entity/zombie.json");
    //     try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir)){
    //         for(Path e : stream){
    //             System.out.println(e.toAbsolutePath());
    //         }
    //     }catch(IOException e){
            
    //     }
    // }
    public static void main(String[] args) throws IOException{
        Path startingDir = Paths.get("resources/graphics/Zombies");
        List<Path> result = new LinkedList<Path>();
        Files.walkFileTree(startingDir, new FindJavaVisitor(result));
        System.out.println("result.size()=" + result.size());        
    }
    
    private static class FindJavaVisitor extends SimpleFileVisitor<Path>{
        private List<Path> result;
        public FindJavaVisitor(List<Path> result){
            this.result = result;
        }
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
            if(file.toString().endsWith(".png")){
                result.add(file.getFileName());
            }
            return FileVisitResult.CONTINUE;
        }
    }
}
